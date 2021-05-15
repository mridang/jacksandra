package com.datastax.spark.connector

import com.datastax.oss.driver.api.querybuilder.{QueryBuilder, SchemaBuilder}
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.writer.{CassandraJsonRowWriterFactory, RowWriterFactory}
import com.dimafeng.testcontainers.{CassandraContainer, ForAllTestContainer}
import com.holdenkarau.spark.testing.SharedSparkContext
import com.mridang.jacksandra.javabeans.CassandraJavaBeanMapper
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class MainSuite extends AnyFunSuite with ForAllTestContainer with SharedSparkContext {

  //noinspection ScalaStyle
  import com.datastax.spark.connector.plus.{toRDDFunctions, toSparkContextFunctions}

  override def container: CassandraContainer = CassandraContainer()
  override val conf: SparkConf = {
    super.conf
      .set("spark.cassandra.connection.host", "helenus.dev.nos.to")
      .set("spark.cassandra.connection.port", "9042")
      .set("spark.cassandra.output.batch.grouping.key", "none")
  }
  final val defaultKeyspace: String = "jacksandra"
  final val tableName: String = "mybean"

  override def setup(sc: SparkContext): Unit = {
    CassandraConnector(conf).withSessionDo(session => {
      val mapper = new CassandraJavaBeanMapper[MyBean](defaultKeyspace)

      session.execute(SchemaBuilder.createKeyspace(defaultKeyspace).ifNotExists()
        .withSimpleStrategy(1)
        .asCql())

      val createSchema: List[String] = mapper.generateMappingProperties
      createSchema.foreach(statement => {
        session.execute(statement)
      })

      val truncateTable: String =
        QueryBuilder.truncate(defaultKeyspace, tableName).build().getQuery
      session.execute(truncateTable)
    })
    super.setup(sc)
  }

  test("that saving and querying entities works as expected") {
    //container.start()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[MyBean] = new CassandraJsonRowWriterFactory[MyBean]

    val inputRDD: RDD[MyBean] = RandomRDD[MyBean](sc).of(numItems = 50)
    val inputItems: Set[MyBean] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace, tableName)
    sc.cassandraTable[MyBean](defaultKeyspace, tableName)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }
}
