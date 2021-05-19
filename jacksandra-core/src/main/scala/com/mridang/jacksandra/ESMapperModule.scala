package com.mridang.jacksandra

import com.fasterxml.jackson.databind.ser.Serializers
import com.fasterxml.jackson.databind.{BeanDescription, JavaType, JsonSerializer, SerializationConfig}
import com.fasterxml.jackson.module.scala.DefaultScalaModule

class ESMapperModule(serializer: CassandraSerializer)
  extends DefaultScalaModule
    with Serializable {

  +=(new Serializers.Base() {
    override def findSerializer(
                                 config: SerializationConfig,
                                 `type`: JavaType,
                                 beanDesc: BeanDescription): JsonSerializer[_] = {
      if (classOf[CassandraMappings].isAssignableFrom(`type`.getRawClass)) {
        serializer
      } else {
        super.findSerializer(config, `type`, beanDesc)
      }
    }
  })
}
