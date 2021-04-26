package com.mridang.jacksandra

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.annotation.JsonFormat.{Shape, Value}
import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.jsonSchema.factories._
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.lang.reflect.Method
import java.time.{LocalDate, LocalDateTime}
import java.util
import scala.reflect.ClassTag

case class MappedMethod(method: Method, elasticsearchMapping: CqlName)

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
class CassandraMapper[T]()(implicit classTag: ClassTag[T]) {
  private val annotationProcessingModule = new ESMapperModule(serializer)
  private val jsonMapper =
    new ObjectMapper()
      .registerModule(annotationProcessingModule)

  private val schemaMapper = new ObjectMapper()
    .registerModule(DefaultScalaModule)
    .registerModule(new Jdk8Module)
    .registerModule(new JavaTimeModule)
    .registerModule(new SimpleModule()
      .setSerializerModifier(new ElasticsearchSchemaBeanSerializerModifier))
    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  schemaMapper
    .configOverride(classOf[LocalDate])
    .setFormat(Value.forShape(Shape.STRING))

  schemaMapper
    .configOverride(classOf[LocalDateTime])
    .setFormat(Value.forShape(Shape.STRING))

  def serializer: CassandraSerializer = new CassandraSerializer

  def wrapperFactory: WrapperFactory =
    new CassandraSchemaFactoryWrapperFactory((provider, factory) =>
      new CassandraSchemaFactoryWrapper(provider, factory))

  def generateMappingProperties: String = {
    val schemaFactoryWrapper: SchemaFactoryWrapper =
      wrapperFactory.getWrapper(schemaMapper.getSerializerProvider)
    schemaMapper.acceptJsonFormatVisitor(
      classTag.runtimeClass,
      schemaFactoryWrapper)

    CassandraSchema.from(schemaFactoryWrapper)
  }

  def map(obj: T): util.Map[String, _] = {
    jsonMapper.convertValue(obj, classOf[util.Map[String, Any]])
  }

  def toJson(obj: T): String = jsonMapper.writeValueAsString(obj)
}
