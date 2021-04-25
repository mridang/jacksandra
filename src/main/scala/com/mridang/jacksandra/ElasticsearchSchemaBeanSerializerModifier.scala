package com.mridang.jacksandra

import com.fasterxml.jackson.databind.ser.BeanSerializerModifier
import com.fasterxml.jackson.databind.{
  BeanDescription,
  JsonSerializer,
  SerializationConfig
}
import com.mridang.jacksandra.javabeans.CassandraRootSchema

class ElasticsearchSchemaBeanSerializerModifier extends BeanSerializerModifier {
  override def modifySerializer(
      config: SerializationConfig,
      beanDesc: BeanDescription,
      serializer: JsonSerializer[_]): JsonSerializer[_] = {
    if (beanDesc.getBeanClass == classOf[CassandraRootSchema]) {
      new TypeOmittingSerializer(
        serializer.asInstanceOf[JsonSerializer[CassandraRootSchema]])
    } else {
      serializer
    }
  }
}
