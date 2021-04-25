package com.mridang.jacksandra

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.{CreateType, CreateTypeStart}
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra.javabeans.CassandraUdtSchema

import scala.collection.JavaConverters.mapAsScalaMapConverter

class CassandraUDTs() {

  def of(name: CqlName, schema: ObjectSchema): Unit = {
    val xx: CreateTypeStart = SchemaBuilder
      .createType(name.value())
      .ifNotExists()

    var createType: CreateType = null
    schema
      .asInstanceOf[CassandraUdtSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .foreach { column =>
        createType = createType match {
          case table: CreateType => {
            table.withField(column.ann.value, column.cassandraType)
          }
          case _ => {
            xx.withField(column.ann.value, column.cassandraType)
          }
        }
      }

    println(createType.build().getQuery)
  }
}