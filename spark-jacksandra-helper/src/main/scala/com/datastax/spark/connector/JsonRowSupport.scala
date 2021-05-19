package com.datastax.spark.connector

import com.fasterxml.jackson.annotation.JsonFormat.{Shape, Value}
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{BeanDescription, JavaType, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.cql.CassandraModule
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.sql.Timestamp
import java.time._
import java.util.Date
import scala.collection.JavaConverters._
import scala.reflect.ClassTag

/**
 * Custom row-writer/reader trait that can be used to decorate the existing
 * `RowReader` and `RowWriter` implementations to denote support for serde
 * between JSON and entities and vice versa.
 *
 * This class has some Jackson-specific foo to get a list of properties i.e.
 * columns, from the bean. You would think that with all the power of Jackson,
 * this would be a one-liner but nah. When introspecting beans to find a list
 * of properties, it returns ALL properties and does not take into account any
 * properties that are excluded e.g. via the `JsonIgnore` annotation.
 *
 * https://stackoverflow.com/q/45834654/304151
 *
 * @tparam T the type of the entity to serialise/deserialize
 * @author mridang
 */
trait JsonRowSupport[T] {

  @transient lazy val objectMapper: ObjectMapper = {
    val schemaMapper = new ObjectMapper()
      .registerModule(DefaultScalaModule)
      .registerModule(new Jdk8Module)
      .registerModule(new JavaTimeModule)
      .registerModule(new SimpleModule())
      .registerModule(new GuavaModule())
      .registerModule(new CassandraModule)
      .enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)

    schemaMapper.configOverride(classOf[Date]).setFormat(Value.forShape(Shape.STRING))
    schemaMapper.configOverride(classOf[Timestamp]).setFormat(Value.forShape(Shape.STRING))
    schemaMapper.configOverride(classOf[Instant]).setFormat(Value.forShape(Shape.STRING))
    schemaMapper.configOverride(classOf[OffsetDateTime]).setFormat(Value.forShape(Shape.STRING))
    schemaMapper.configOverride(classOf[ZonedDateTime]).setFormat(Value.forShape(Shape.STRING))
    schemaMapper.configOverride(classOf[Duration]).setFormat(Value.forShape(Shape.STRING)) // So we can store nanosecond precision. Queryable
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

    schemaMapper
  }
  val ct: ClassTag[T]

  def columnNames: Seq[String] = {
    val jType: JavaType =
      objectMapper.getTypeFactory.constructType(ct.runtimeClass)
    val beanDesc: BeanDescription =
      objectMapper.getSerializationConfig.introspect[BeanDescription](jType)
    val ignoredProps: Set[String] = getIgnoredProperties(beanDesc)
    val properties: Seq[String] = getDescribedProperties(beanDesc)
    properties diff ignoredProps.toList
  }

  private def getDescribedProperties(
                                      beanDescription: BeanDescription): List[String] = {
    beanDescription.findProperties.asScala
      .filter(propDef => propDef.getAccessor != null)
      .map(pr => pr.getName)
      .toList
  }

  private def getIgnoredProperties(
                                    beanDescription: BeanDescription): Set[String] = {
    val introspector =
      objectMapper.getSerializationConfig.getAnnotationIntrospector
    val classInfo: AnnotatedClass = beanDescription.getClassInfo
    Option(introspector.findPropertyIgnorals(classInfo)) match {
      case Some(value) => value.getIgnored.asScala.toSet
      case None => Set.empty
    }
  }
}
