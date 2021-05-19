package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.mridang.jacksandra.annotations.{OrderedClusteringColumn, StaticColumn}
import com.mridang.jacksandra.javabeans.CassandraSchema

import scala.annotation.meta.field

//noinspection DuplicatedCode
class CassandraSetSchema(
                          @(JsonIgnore@field) override val name: CqlName,
                          @(JsonIgnore@field) override val backing: JsonSchema,
                          override val isPartitionKey: Option[PartitionKey],
                          override val clusteringColumn: Option[OrderedClusteringColumn],
                          override val staticColumn: Option[StaticColumn])
  extends CassandraJsonSchemaBase(
    name,
    isPartitionKey,
    clusteringColumn,
    staticColumn)
    with CassandraContainerSchema {

  override def cassandraType: DataType = {
    backing match {
      case cassandraColumn: CassandraSchema =>
        DataTypes.listOf(cassandraColumn.getDataType)
      case cassandraColumn: CassandraItemSchema =>
        DataTypes.listOf(cassandraColumn.getDataType)
      case _ => {
        println("random column encountered" + name.value)
        DataTypes.listOf(DataTypes.TEXT)
      }
    }
  }
}
