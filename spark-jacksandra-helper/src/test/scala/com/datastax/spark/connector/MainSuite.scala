package com.datastax.spark.connector

import com.datastax.oss.driver.api.querybuilder.SchemaBuilder
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.writer.{CassandraJsonRowWriterFactory, RowWriterFactory}
import com.dimafeng.testcontainers.{CassandraContainer, ForAllTestContainer}
import com.holdenkarau.spark.testing.SharedSparkContext
import com.mridang.jacksandra.javabeans.CassandraJavaBeanMapper
import com.mridang.jacksandra.{JavaBeanWithCollections, JavaBeanWithExotics, JavaBeanWithNumbers, JavaBeanWithTemporal, JavaBeanWithUDT}
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.must.Matchers.contain
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

import scala.reflect.ClassTag

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
  final val randomItemsCount: Int = 1

  def createTable[T]()(implicit ctag: ClassTag[T]): Unit = {
    CassandraConnector(conf).withSessionDo(session => {
      val mapper = new CassandraJavaBeanMapper[T](defaultKeyspace)

      session.execute(SchemaBuilder.dropKeyspace(defaultKeyspace).ifExists()
        .asCql())

      session.execute(SchemaBuilder.createKeyspace(defaultKeyspace).ifNotExists()
        .withSimpleStrategy(1)
        .asCql())

      val createSchema: List[String] = mapper.generateMappingProperties
      createSchema.foreach(statement => {
        session.execute(statement)
      })
    })
  }

  test("that saving and querying entities works as expected") {
    //container.start()
    createTable[MyBean]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[MyBean] = new CassandraJsonRowWriterFactory[MyBean]

    val inputRDD: RDD[MyBean] = RandomRDD[MyBean](sc).of(randomItemsCount)
    val inputItems: Set[MyBean] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[MyBean](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with collections works as expected") {
    //container.start()
    createTable[JavaBeanWithCollections]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[JavaBeanWithCollections] = new CassandraJsonRowWriterFactory[JavaBeanWithCollections]

    val inputRDD: RDD[JavaBeanWithCollections] = RandomRDD[JavaBeanWithCollections](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithCollections] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[JavaBeanWithCollections](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with numbers works as expected") {
    //container.start()
    createTable[JavaBeanWithNumbers]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[JavaBeanWithNumbers] = new CassandraJsonRowWriterFactory[JavaBeanWithNumbers]

    val inputRDD: RDD[JavaBeanWithNumbers] = RandomRDD[JavaBeanWithNumbers](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithNumbers] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[JavaBeanWithNumbers](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with temporals works as expected") {
    //container.start()
    createTable[JavaBeanWithTemporal]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[JavaBeanWithTemporal] = new CassandraJsonRowWriterFactory[JavaBeanWithTemporal]

    val inputRDD: RDD[JavaBeanWithTemporal] = RandomRDD[JavaBeanWithTemporal](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithTemporal] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[JavaBeanWithTemporal](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with udts works as expected") {
    //container.start()
    createTable[JavaBeanWithUDT]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[JavaBeanWithUDT] = new CassandraJsonRowWriterFactory[JavaBeanWithUDT]

    val inputRDD: RDD[JavaBeanWithUDT] = RandomRDD[JavaBeanWithUDT](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithUDT] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[JavaBeanWithUDT](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }

  test("that saving and querying entities with exotics works as expected") {
    //container.start()
    createTable[JavaBeanWithExotics]()

    implicit val connector: CassandraConnector = CassandraConnector(conf)
    implicit val rwf: RowWriterFactory[JavaBeanWithExotics] = new CassandraJsonRowWriterFactory[JavaBeanWithExotics]

    val inputRDD: RDD[JavaBeanWithExotics] = RandomRDD[JavaBeanWithExotics](sc).of(randomItemsCount)
    val inputItems: Set[JavaBeanWithExotics] = inputRDD.collect()
      .toSet

    inputRDD.saveToCassandra(defaultKeyspace)
    sc.cassandraTable[JavaBeanWithExotics](defaultKeyspace)
      .collect()
      .toSet should contain theSameElementsAs inputItems
  }
}
