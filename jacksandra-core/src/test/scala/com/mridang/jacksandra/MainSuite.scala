package com.mridang.jacksandra

import com.mridang.jacksandra.javabeans.CassandraJavaBeanMapper
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper

class MainSuite extends AnyFunSuite {

  final val defaultKeyspace: String = "jacksandra"

  /**
   * Utility method to compare two queries by replacing all spaces and newlines
   * as one of the queries is formatted for readability by generated one isn't
   *
   * @param actualQuery the actual hard-coded query to assert against
   * @param expectedQuery the generated query to assert with
   */
  def compare(actualQuery: String, expectedQuery: String): Unit = {
    val actualQueryNorm = actualQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")
    val expectedQueryNorm = expectedQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")
    actualQueryNorm shouldEqual expectedQueryNorm
  }

  test("that all numeric types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[JavaBeanWithNumbers](defaultKeyspace)
    val ddl: String = mapper.generateMappingProperties.mkString
    //noinspection ScalaStyle
    val query =
    """ CREATE
      |  TABLE
      | IF NOT
      | EXISTS jacksandra.myjavabeanwithnumbers
      |      ( mypartitionkey           TEXT                 PRIMARY KEY
      |      , toint                    INT
      |      , todouble                 DOUBLE
      |      , tosmallint               SMALLINT
      |      , totinyint                TINYINT
      |      , tofloat                  FLOAT
      |      , tobigint                 BIGINT
      |      , tovarint                 VARINT
      |      , tobigdecimal             DECIMAL
      |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all collection types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[JavaBeanWithCollections](defaultKeyspace)
    val ddl: String = mapper.generateMappingProperties.mkString
    //noinspection ScalaStyle
    val query =
    """ CREATE
      |  TABLE
      | IF NOT
      | EXISTS jacksandra.myjavabeanwithcollections
      |      ( mypartitionkey           TEXT                 PRIMARY KEY
      |      , toliststring             LIST<TEXT>
      |      , tosetstring              SET<TEXT>
      |      , toimmutablesetstring     SET<TEXT>
      |      , tofrozenliststring       FROZEN<LIST<TEXT>>
      |      , tosetliststring          FROZEN<SET<TEXT>>
      |      , toimmutableliststring    LIST<TEXT>
      |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all temporal types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[JavaBeanWithTemporal](defaultKeyspace)
    val ddl: String = mapper.generateMappingProperties.mkString
    //noinspection ScalaStyle
    val query =
      """CREATE
        | TABLE
        |IF NOT
        |EXISTS jacksandra.myjavabeanwithtemporals
        |     ( mypartitionkey TEXT PRIMARY KEY
        |     , tolocaldatetimetimestamp TEXT
        |     , totimestamptimestamp TEXT
        |     , todatetimestamp TEXT
        |     , tolocaltimetime TIME
        |     , toperioddaterange TEXT
        |     , toinstanttimestamp TIMESTAMP
        |     , tozoneidstring TEXT
        |     , toyearstring TEXT
        |     , tozoneoffsetstring TEXT
        |     , toyearmonthstring TEXT
        |     , tozoneddatetimestring TEXT
        |     , tomonthdaystring TEXT
        |     , tooffsettimestring TEXT
        |     , tojavadurationduration TEXT
        |     , tooffsetdatetimestring TEXT
        |     , tolocaldatedate DATE
        |     )
        """.stripMargin

    compare(query, ddl)
  }

  test("that all custom types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[JavaBeanWithUDT](defaultKeyspace)
    val ddl: String = mapper.generateMappingProperties.mkString("\n")
    //noinspection ScalaStyle
    val query =
      """
        | CREATE
        |   TYPE
        | IF NOT
        | EXISTS jacksandra.myudt
        |      ( somelonglist           FROZEN<LIST<BIGINT>>
        |      , someintegerlist        LIST<INT>
        |      , somestring             TEXT
        |      , somedoubleset          FROZEN<SET<DOUBLE>>
        |      , somedouble             DOUBLE
        |      , somefloatset           SET<FLOAT>
        |      )
        |
        | CREATE
        |  TABLE
        | IF NOT
        | EXISTS jacksandra.myjavabeanwithudt
        |      ( mypartitionkey         TEXT                 PRIMARY KEY
        |      , tofrozenudtlist        FROZEN<LIST<myudt>>
        |      , toudtset               SET<myudt>
        |      , toudtlist              LIST<myudt>
        |      , toudt                  myudt
        |      , tofrozenudtset         FROZEN<SET<myudt>>
        |      )
      """.stripMargin

    compare(query, ddl)
  }
}