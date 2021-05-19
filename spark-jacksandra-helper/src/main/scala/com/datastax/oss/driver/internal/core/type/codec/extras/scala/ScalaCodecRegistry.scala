package com.datastax.oss.driver.internal.core.`type`.codec.extras.scala

import com.datastax.oss.driver.api.core.`type`.codec.CodecNotFoundException
import com.datastax.oss.driver.api.core.`type`.reflect.GenericType
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.internal.core.`type`.codec.extras.CassandraCodecRegistry
import com.fasterxml.jackson.databind.JavaType

class ScalaCodecRegistry extends CassandraCodecRegistry with (JavaType => DataType) {

  register(new BigDecimalCodec)
  register(new BigIntCodec)

  override def apply(javaType: JavaType): DataType = {
    val javaClass: Class[_] = javaType.getRawClass
    val udtName = Option(javaClass.getAnnotation(classOf[CqlName]))
    udtName match {
      case Some(name) => QueryBuilder.udt(name.value())
      case None =>
        try {
          val gt: GenericType[_] = GenericType.of(javaClass)
          codecFor(gt).getCqlType
        } catch {
          case e: CodecNotFoundException =>
            println(e.getMessage)
            DataTypes.TEXT
          case e: Exception =>
            e.printStackTrace()
            DataTypes.TEXT
        }
    }
  }
}
