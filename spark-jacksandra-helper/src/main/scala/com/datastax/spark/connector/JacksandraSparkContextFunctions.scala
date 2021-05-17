package com.datastax.spark.connector

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.rdd.reader.{CassandraJsonRowReaderFactory, RowReaderFactory}
import com.datastax.spark.connector.rdd.{AbstractCassandraRDD, CqlWhereClause, ReadConf}
import org.apache.spark.SparkContext

import scala.reflect.ClassTag

class JacksandraSparkContextFunctions(@transient val sc: SparkContext) extends Serializable {

  def cassandraTable[T](keyspace: String)(
    implicit connector: CassandraConnector = CassandraConnector(sc),
    readConf: ReadConf = ReadConf.fromSparkConf(sc.getConf),
    classTag: ClassTag[T]):
  AbstractCassandraRDD[T] = {

    val tableName = classTag.runtimeClass.getAnnotation(classOf[CqlName]).value()

    new AbstractCassandraRDD[T](sc, keyspace, tableName)(classTag = classTag) {
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