package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.DataType
import com.datastax.oss.driver.api.mapper.annotations.{CqlName, PartitionKey}
import com.mridang.jacksandra.annotations.{OrderedClusteringColumn, StaticColumn}

class CassandraColumnSchema(
                             override val ann: CqlName,
                             override val cassandraType: DataType,
                             override val isPartitionKey: Option[PartitionKey],
                             override val clusteringColumn: Option[OrderedClusteringColumn],
                             override val staticColumn: Option[StaticColumn])
  extends CassandraJsonSchemaBase(
    ann,
    isPartitionKey,
    clusteringColumn,
    staticColumn)
