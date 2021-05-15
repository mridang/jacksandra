package com.datastax.spark.connector

import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.writer.{CassandraTableWriter, RowWriterFactory, WritableToCassandra, WriteConf}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

class CassandraRDDFunctions[T](rdd: RDD[T])
    extends WritableToCassandra[T]
    with Serializable {

  val sparkContext: SparkContext = rdd.sparkContext

  def saveToCassandra(keyspaceName: String,
                      tableName: String,
                      columns: ColumnSelector = AllColumns,
                      writeConf: WriteConf =
                        WriteConf.fromSparkConf(sparkContext.getConf))(
      implicit connector: CassandraConnector = CassandraConnector(sparkContext),
      rwf: RowWriterFactory[T]): Unit = {

    val writer =
      CassandraTableWriter(connector,
                           keyspaceName,
                           tableName,
                           columns,
                           writeConf)

    rdd.sparkContext.runJob(rdd, writer.write _)
  }

  override def deleteFromCassandra(keyspaceName: String,
                                   tableName: String,
                                   deleteColumns: ColumnSelector,
                                   keyColumns: ColumnSelector,
                                   writeConf: WriteConf)(
      implicit connector: CassandraConnector,
      rwf: RowWriterFactory[T]): Unit = ???
}
