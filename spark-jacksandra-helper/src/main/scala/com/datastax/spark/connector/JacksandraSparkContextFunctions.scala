package com.datastax.spark.connector

import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.rdd.reader.{CassandraJsonRowReaderFactory, RowReaderFactory}
import com.datastax.spark.connector.rdd.{AbstractCassandraRDD, CqlWhereClause, ReadConf}
import org.apache.spark.SparkContext

import scala.reflect.ClassTag

class JacksandraSparkContextFunctions(@transient val sc: SparkContext) extends Serializable {

  def cassandraTable[T](keyspace: String, tableName: String)(
    implicit connector: CassandraConnector = CassandraConnector(sc),
    readConf: ReadConf = ReadConf.fromSparkConf(sc.getConf),
    ct: ClassTag[T]):
  AbstractCassandraRDD[T] = {

    new AbstractCassandraRDD[T](sc, keyspace, tableName)(classTag = ct) {
      override def buildWhereClause(index: Int): CqlWhereClause = {
        CqlWhereClause.empty
      }

      override def buildWhereClause(merchant: String): CqlWhereClause = {
        CqlWhereClause.empty
      }

      override protected val rowReaderFactory: RowReaderFactory[T] = {
        new CassandraJsonRowReaderFactory[T]()(ct = classTag)
      }
    }
  }
}
