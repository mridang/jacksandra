package com.mridang.jacksandra

import com.mridang.jacksandra.javabeans.CassandraJavaBeanMapper
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainSuite extends FunSuite {

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

  ignore("that all numeric types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[BeanWithNumbers]()
    val ddl: String = mapper.generateMappingProperties
    //noinspection ScalaStyle
    val query =
    """ CREATE
      |  TABLE
      | IF NOT
      | EXISTS cunt
      |      ( mypartitionkey           TEXT                 PRIMARY KEY
      |      , toint                    INT
      |      , todouble                 DOUBLE
      |      , tosmallint               SMALLINT
      |      , totinyint                TINYINT
      |      , tofloat                  FLOAT
      |      , tobigint                 BIGINT
      |      , tovarint                 VARINT
      |      , tobigdecimal             DECIMAL
      |      );
      """.stripMargin

    compare(query, ddl)
  }

  ignore("that all collection types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[BeanWithCollections]()
    val ddl: String = mapper.generateMappingProperties
    //noinspection ScalaStyle
    val query =
    """ CREATE
      |  TABLE
      | IF NOT
      | EXISTS cunt
      |      ( mypartitionkey           TEXT                 PRIMARY KEY
      |      , toliststring             LIST<TEXT>
      |      , tosetstring              SET<TEXT>
      |      , toimmutablesetstring     SET<TEXT>
      |      , tofrozenliststring       FROZEN<LIST<TEXT>>
      |      , tosetliststring          FROZEN<SET<TEXT>>
      |      , toimmutableliststring    LIST<TEXT>
      |      );
      """.stripMargin

    compare(query, ddl)
  }

  test("that all custom types are handled correctly") {
    val mapper = new CassandraJavaBeanMapper[BeanWithUDT]()
    val ddl: String = mapper.generateMappingProperties
    //noinspection ScalaStyle
    val query =
      """
        | CREATE
        |   TYPE
        | IF NOT
        | EXISTS myudt
        |      ( somelonglist           FROZEN<LIST<BIGINT>>
        |      , someintegerlist        LIST<INT>
        |      , somestring             TEXT
        |      , somedoubleset          FROZEN<SET<DOUBLE>>
        |      , somedouble             DOUBLE
        |      , somefloatset           SET<FLOAT>
        |      );
        |
        | CREATE
        |  TABLE
        | IF NOT
        | EXISTS cunt
        |      ( mypartitionkey         TEXT                 PRIMARY KEY
        |      , toudtlist              LIST<myudt>
        |      , toudt                  myudt
        |      );
      """.stripMargin

    println(ddl)
    compare(query, ddl)
  }
}