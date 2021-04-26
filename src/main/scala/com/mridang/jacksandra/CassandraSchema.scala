package com.mridang.jacksandra

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra.javabeans.{CassandraJavaBeanSchemaFactoryWrapper, CassandraTable}

import scala.compat.Platform

object CassandraSchema {

  def from(schemaFactoryWrapper: SchemaFactoryWrapper): List[String] =  {
    val types: List[String] = schemaFactoryWrapper
      .asInstanceOf[CassandraJavaBeanSchemaFactoryWrapper]
      .cassandraVisitorContext
      .asInstanceOf[CassandraVisitorContext]
      .udts
      .map { (f: (CqlName, ObjectSchema)) =>
        new CassandraUDTs().of(f._1, f._2) + Platform.EOL
      }
      .toList

    val table: String = new CassandraTable(schemaFactoryWrapper.finalSchema())
      .buildSchema + Platform.EOL

    types ++ List(table)
  }
}
