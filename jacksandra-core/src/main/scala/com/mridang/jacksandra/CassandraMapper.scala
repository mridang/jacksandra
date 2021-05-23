package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.reflect.GenericType
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.internal.core.`type`.codec.extras.CassandraCodecRegistry
import com.datastax.oss.driver.internal.core.`type`.codec.registry.DefaultCodecRegistry
import com.fasterxml.jackson.annotation.JsonFormat.{Shape, Value}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.cql.CassandraModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.jsonSchema.factories._
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.sql.Timestamp
import java.time._
import java.util
import java.util.Date
import scala.reflect.ClassTag

object CassandraMapper {

  final val codecRegistry: DefaultCodecRegistry = {
    new CassandraCodecRegistry
  }

  def getDT(javaType: JavaType): DataType = {
    val xx: Class[_] = javaType.getRawClass
    val udtName = Option(xx.getAnnotation(classOf[CqlName]))
    udtName match {
      case Some(name) => QueryBuilder.udt(name.value())
      case None =>
        try {
          val gt: GenericType[_] = GenericType.of(xx)
          codecRegistry.codecFor(gt).getCqlType
        } catch {
          case e: Exception => {
            println("Had an IOException trying to read that file" + e)
          }
            DataTypes.TEXT
        }
    }
  }
}

/**
 * A class for mapping scala pojos to elasticsearch query compatible objects.
 * The mapping is marked using [[CqlName]] annotations. Note that
 * we rely on java reflection instead of scala reflection since the scala API
 * still seems pretty experimental. Especially fetching annotation values in
 * a robust way turned out to be a nightmare.
 *
 * This class can do the following:
 * - Generate a map from the given class that can be serialized to JSON and used in Cassandra mapping definitions.
 * - Generate a map from a given instance that can be serialized to JSON and used in Cassandra index queries.
 *
 * @tparam T The class to be mapped
 */
class CassandraMapper[T](keyspace: String, dataFn: JavaType => DataType = CassandraMapper.getDT)(implicit classTag: ClassTag[T]) {

  private val schemaMapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .registerModule(new Jdk8Module)
    .registerModule(new JavaTimeModule)
    .registerModule(new SimpleModule())
    .registerModule(new CassandraModule)
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)


  schemaMapper.configOverride(classOf[Date]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[Timestamp]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[Instant]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[OffsetDateTime]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[ZonedDateTime]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[Duration]).setFormat(Value.forShape(Shape.STRING)) // Use CqlDuration instead
  schemaMapper.configOverride(classOf[LocalDateTime]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[LocalDate]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[LocalTime]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[MonthDay]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string (or array) and it is easy to query when it is a string
  schemaMapper.configOverride(classOf[OffsetTime]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[Period]).setFormat(Value.forShape(Shape.STRING))
  schemaMapper.configOverride(classOf[Year]).setFormat(Value.forShape(Shape.NUMBER_INT)) // Storing as number allows querying
  schemaMapper.configOverride(classOf[YearMonth]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string (or array) and it is easy to query when it is a string
  schemaMapper.configOverride(classOf[ZoneId]).setFormat(Value.forShape(Shape.STRING)) // Jackson only serializes to string and it is easy to query when it is a string
  schemaMapper.configOverride(classOf[ZoneOffset]).setFormat(Value.forShape(Shape.STRING))

  def generateMappingProperties: List[String] = {
    val schemaFactoryWrapper: SchemaFactoryWrapper =
      wrapperFactory.getWrapper(schemaMapper.getSerializerProvider)
    schemaMapper.acceptJsonFormatVisitor(
      classTag.runtimeClass,
      schemaFactoryWrapper)

    CassandraSchema.from(keyspace, schemaFactoryWrapper)
  }

  def wrapperFactory: WrapperFactory =
    new CassandraSchemaFactoryWrapperFactory((provider, factory) =>
      new CassandraSchemaFactoryWrapper(provider, factory, dataFn))

  def map(obj: T): util.Map[String, _] = {
    schemaMapper.convertValue(obj, classOf[util.Map[String, Any]])
  }

  def toJson(obj: T): String = schemaMapper.writeValueAsString(obj)
}
