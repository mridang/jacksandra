package com.datastax.spark.connector

import com.fasterxml.jackson.annotation.JsonFormat.{Shape, Value}
import com.fasterxml.jackson.databind.introspect.AnnotatedClass
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.{BeanDescription, JavaType, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule

import java.time.{LocalDate, LocalDateTime}
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
  *
  * @author mridang
  */
trait JsonRowSupport[T] {

  val ct: ClassTag[T]

  @transient lazy val objectMapper: ObjectMapper = {
    val schemaMapper = new ObjectMapper()
      .registerModule(DefaultScalaModule)
      .registerModule(new Jdk8Module)
      .registerModule(new JavaTimeModule)
      .registerModule(new SimpleModule())
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

    schemaMapper
      .configOverride(classOf[LocalDate])
      .setFormat(Value.forShape(Shape.STRING))

    schemaMapper
      .configOverride(classOf[LocalDateTime])
      .setFormat(Value.forShape(Shape.STRING))

    schemaMapper
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

  def columnNames: Seq[String] = {
    val jType: JavaType =
      objectMapper.getTypeFactory.constructType(ct.runtimeClass)
    val beanDesc: BeanDescription =
      objectMapper.getSerializationConfig.introspect[BeanDescription](jType)
    val ignoredProps: Set[String] = getIgnoredProperties(beanDesc)
    val properties: Seq[String] = getDescribedProperties(beanDesc)
    properties diff ignoredProps.toList
  }
}
