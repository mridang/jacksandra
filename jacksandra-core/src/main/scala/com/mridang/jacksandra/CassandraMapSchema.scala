package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra.annotations.{OrderedClusteringColumn, StaticColumn}

import scala.annotation.meta.field

class CassandraMapSchema(
                          @(JsonIgnore@field) override val name: CqlName,
                          @(JsonIgnore@field) override val backing: ObjectSchema,
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
      case cassandraColumn: CassandraEntrySchema =>
        cassandraColumn.getDataType
      case _ =>
        throw new RuntimeException("Unexpected type")
    }
  }
}
