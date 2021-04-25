package com.mridang.jacksandra

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.jsontype.TypeSerializer
import com.fasterxml.jackson.databind.{JsonSerializer, SerializerProvider}
import com.mridang.jacksandra.javabeans.CassandraRootSchema

/**
  * As of Jackson 2.9, we can't or we don't have to use [[com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver]] / [[com.fasterxml.jackson.module.jsonSchema.JsonSchemaIdResolver]]
  * hack to overwrite the type property. Instead, omit type info completely by wiring serializeWithType to non-typed implementation
  * and simply add type as a regular property.
  */
class TypeOmittingSerializer(backing: JsonSerializer[CassandraRootSchema])
    extends JsonSerializer[CassandraRootSchema] {
  override def serialize(
      value: CassandraRootSchema,
      gen: JsonGenerator,
      serializers: SerializerProvider): Unit =
    backing.serialize(value, gen, serializers)

  override def serializeWithType(
      value: CassandraRootSchema,
      gen: JsonGenerator,
      serializers: SerializerProvider,
      typeSer: TypeSerializer): Unit = {
    backing.serialize(value, gen, serializers)
  }
}
