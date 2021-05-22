package com.mridang.jacksandra.javabeans

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.{BeanProperty, JavaType, JsonSerializer, SerializerProvider}
import com.fasterxml.jackson.module.jsonSchema.factories.{VisitorContext, WrapperFactory}
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra._

import java.beans.Introspector
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import scala.reflect.ClassTag

class CassandraJavaBeanMapper[T](keyspace: String, dataTypeFn: JavaType => DataType = CassandraMapper.getDT)(implicit classTag: ClassTag[T])
  extends CassandraMapper[T](keyspace = keyspace, dataFn = dataTypeFn) {

  override def serializer: CassandraSerializer =
    new CassandraSerializer with JavaESAnnotatedBeanSupport

  override def wrapperFactory: WrapperFactory =
    new CassandraSchemaFactoryWrapperFactory((provider, factory) =>
      new CassandraJavaBeanSchemaFactoryWrapper(provider, factory, dataTypeFn))
}

trait JavaESAnnotatedBeanSupport {
  this: CassandraSerializer =>

  override def serialize(
                          t: CassandraMappings,
                          jsonGenerator: JsonGenerator,
                          serializerProvider: SerializerProvider): Unit = {

    def writeObject(obj: Any, annotation: CqlName): Unit = {
      jsonGenerator.writeFieldName(annotation.value())

      (obj, annotation) match {
        //        case (d: LocalDateTime, prop) if prop.format().isEmpty =>
        //          jsonGenerator.writeNumber(d.toInstant(ZoneOffset.UTC).toEpochMilli)
        //        case (t: Temporal, prop) if !prop.format().isEmpty =>
        //          jsonGenerator.writeString(
        //            DateTimeFormatter.ofPattern(prop.format()).format(t))
        //        case (s: String, prop) if prop.name() == CassandraJavaBeanMapper.ID =>
        //          jsonGenerator.writeString(getIdTrimmed(s))
        case (o, _) => jsonGenerator.writeObject(o)
      }
    }

    jsonGenerator.writeStartObject()
    getAnnotatedMethods(t.getClass).foreach {
      case (m, a: CqlName) =>
        m.invoke(t) match {
          case Some(obj) => //noinspection RedundantBlock
          {
            writeObject(obj, a)
          }
          case None => Unit
          case x if x != null => //noinspection RedundantBlock
          {
            writeObject(x, a)
          }
          case null =>
        }
      case (_, a)
        if a
          .annotationType()
          .getName == "jdk.internal.HotSpotIntrinsicCandidate" =>
      // see https://bugs.openjdk.java.net/browse/JDK-8076112
      // with scala 2.12, the jvm target compatibility is still 1.8
      // so we need to match with the string class name
      case (_, a)
        if a
          .annotationType()
          .getName == "com.fasterxml.jackson.annotation.JsonProperty" =>
    }

    jsonGenerator.writeEndObject()
  }

  private def getAnnotatedMethods(c: Class[_]): Array[(Method, Annotation)] = {
    val properties = Introspector.getBeanInfo(c)

    val fieldAnnotations =
      properties.getBeanDescriptor.getBeanClass.getDeclaredFields
        .map(x => (x.getName, x.getDeclaredAnnotations))
        .toMap

    properties.getPropertyDescriptors.flatMap(
      descriptor =>
        fieldAnnotations
          .getOrElse(descriptor.getName, Array.empty)
          .map(annotation => (descriptor.getReadMethod, annotation)))
  }

  override def createContextual(
                                 prov: SerializerProvider,
                                 prop: BeanProperty): JsonSerializer[_] = {
    new CassandraSerializer with JavaESAnnotatedBeanSupport
  }
}


class CassandraJavaBeanSchemaObjectVisitor(
                                            provider: SerializerProvider,
                                            schema: ObjectSchema,
                                            wrapperFactory: WrapperFactory,
                                            visitorContext: VisitorContext)
  extends CassandraSchemaObjectVisitor(
    provider,
    schema,
    wrapperFactory,
    visitorContext) {

  /**
   * Falling back to the same property handling as for optional property
   * (needed for Java bean support)
   */
  override def property(prop: BeanProperty): Unit = {
    optionalProperty(prop)
  }
}
