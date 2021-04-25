package com.mridang.jacksandra.javabeans

import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import com.datastax.oss.driver.internal.querybuilder.schema.DefaultCreateTable
import com.fasterxml.jackson.module.jsonSchema.JsonSchema
import com.mridang.jacksandra.CassandraJsonSchemaBase

import scala.collection.JavaConverters.mapAsScalaMapConverter

class MySchema(schema: JsonSchema) {

  def getOps: String = {
    val xx = SchemaBuilder
      .createTable("cunt")
      .ifNotExists()

    var createTable: CreateTable = null
    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.isPartitionKey.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.isPartitionKey
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .foreach { column =>
        createTable = createTable match {
          case table: CreateTable => {
            table.withPartitionKey(column.ann.value, column.cassandraType)
          }
          case _ => {
            xx.withPartitionKey(column.ann.value, column.cassandraType)
          }
        }
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.clusteringColumn.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.clusteringColumn
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .foreach { column =>
        createTable = createTable.withClusteringColumn(
          column.ann.value,
          column.cassandraType)
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.staticColumn.nonEmpty
      }
      .foreach { column =>
        createTable =
          createTable.withStaticColumn(column.ann.value, column.cassandraType)
      }

    schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.isPartitionKey.isEmpty
      }
      .filter { column =>
        column.clusteringColumn.isEmpty
      }
      .filter { column =>
        column.staticColumn.isEmpty
      }
      .foreach { column =>
        createTable match {
          case table: DefaultCreateTable => {
            createTable =
              createTable.withColumn(column.ann.value, column.cassandraType)
          }
          case _ =>
            throw new Exception("No partition keys specified")
        }
      }

    // Build the clustering order statement by first finding all properties and
    // then getting the sort order
    val clusteringOrder: Map[String, ClusteringOrder] = schema
      .asInstanceOf[CassandraRootSchema]
      .getProperties
      .asScala
      .filter { (property: (String, JsonSchema)) =>
        property._2.isInstanceOf[CassandraJsonSchemaBase]
      }
      .mapValues { l =>
        l.asInstanceOf[CassandraJsonSchemaBase]
      }
      .values
      .filter { column =>
        column.clusteringColumn.nonEmpty
      }
      .toList
      .sortBy { column: CassandraJsonSchemaBase =>
        column.clusteringColumn
          .map { pk =>
            pk.value()
          }
          .orElse {
            Option(0)
          }
      }
      .map((k: CassandraJsonSchemaBase) => {
        val clusteringOrder = k.clusteringColumn match {
          case Some(value) => {
            if (value.isAscending) {
              ClusteringOrder.ASC
            } else {
              ClusteringOrder.DESC
            }
          }
          case None => ClusteringOrder.ASC
        }
        (k.ann.value(), clusteringOrder)
      })
      .toMap

    createTable.withClusteringOrder(scala.collection.JavaConverters.mapAsJavaMap(clusteringOrder)).asCql()
  }
}
