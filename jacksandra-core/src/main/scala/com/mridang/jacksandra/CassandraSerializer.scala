package com.mridang.jacksandra

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import com.fasterxml.jackson.databind.{BeanProperty, JsonSerializer, SerializerProvider}

import java.beans.Introspector
import java.lang.annotation.Annotation
import java.lang.reflect.Method
import scala.collection.mutable

class CassandraSerializer()
  extends com.fasterxml.jackson.databind.ser.std.StdSerializer[CassandraMappings](classOf[CassandraMappings])
    with ContextualSerializer {

  private val annotatedMethods =
    new mutable.HashMap[Class[_], Array[(Method, Annotation)]]

  override def serialize(
                          t: CassandraMappings,
                          jsonGenerator: com.fasterxml.jackson.core.JsonGenerator,
                          serializerProvider: SerializerProvider): Unit = {
    jsonGenerator.writeStartObject()

    getAnnotatedMethods(t.getClass).foreach {
      case (m, a) if a.annotationType() == classOf[CqlName] =>
        m.invoke(t) match {
          case Some(obj) => //noinspection RedundantBlock
          {
            jsonGenerator.writeFieldName(a.asInstanceOf[CqlName].value())
            jsonGenerator.writeObject(obj)
          }
          case None => Unit
          case x if x != null => //noinspection RedundantBlock
          {
            jsonGenerator.writeFieldName(a.asInstanceOf[CqlName].value())
            jsonGenerator.writeObject(x)
          }
          case null => //noinspection RedundantBlock
          {
            //noinspection RedundantBlock
            println(
              s"Omitting null field ${a.asInstanceOf[CqlName].value()} on object ${t}")
          }
        }
      case (_, a)
        if a
          .annotationType()
          .getName == "jdk.internal.HotSpotIntrinsicCandidate" =>
      // see https://bugs.openjdk.java.net/browse/JDK-8076112
      // with scala 2.12, the jvm target compatibility is still 1.8
      // so we need to match with the string class name
    }

    jsonGenerator.writeEndObject()
  }

  private def getAnnotatedMethods(c: Class[_]) = this.synchronized {
    annotatedMethods.getOrElseUpdate(
      c,
      Introspector
        .getBeanInfo(c)
        .getMethodDescriptors
        .map(_.getMethod)
        .flatMap(m => m.getDeclaredAnnotations.map(a => (m, a))))
  }

  override def createContextual(
                                 prov: SerializerProvider,
                                 property: BeanProperty): JsonSerializer[_] = {
    new CassandraSerializer
  }
}
