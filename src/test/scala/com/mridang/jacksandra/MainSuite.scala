package com.mridang.jacksandra

import com.mridang.jacksandra.javabeans.CassandraJavaBeanMapper
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.Matchers.convertToAnyShouldWrapper
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MainSuite extends FunSuite {

  def compare(actualQuery: String, expectedQuery: String): Unit = {
    val actualQueryNorm = actualQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")
    val expectedQueryNorm = expectedQuery.toLowerCase.trim.replaceAll("[\\n\\s]*", "")
    actualQueryNorm shouldEqual expectedQueryNorm
  }

  ignore("that all numeric types are handled correctly") {
    val mapper =
      new CassandraJavaBeanMapper[BeanWithNumbers]()
    val ddl: String = mapper.generateMappingProperties
    //noinspection ScalaStyle
    val query = """ CREATE
      |  TABLE
      | IF NOT
      | EXISTS cunt
      |      ( mypartitionkey TEXT     PRIMARY KEY
      |      , toint           INT
      |      , todouble        DOUBLE
      |      , tosmallint      SMALLINT
      |      , totinyint       TINYINT
      |      , tofloat         FLOAT
      |      , tobigint        BIGINT
      |      , tovarint        VARINT
      |      , tobigdecimal    DECIMAL
      |      )
      """.stripMargin

    compare(query, ddl)
  }

  test("that all collection types are handled correctly") {
    val mapper =
      new CassandraJavaBeanMapper[BeanWithCollections]()
    val ddl: String = mapper.generateMappingProperties
    //noinspection ScalaStyle
    val query = """ CREATE
                  |  TABLE
                  | IF NOT
                  | EXISTS cunt
                  |      ( mypartitionkey           TEXT                 PRIMARY KEY
                  |      , toliststring             SET<TEXT>
                  |      , tofrozenliststring       FROZEN<SET<TEXT>>
                  |      , toimmutableliststring    SET<TEXT>
                  |      )
      """.stripMargin

    println(ddl)
    compare(query, ddl)
  }
}
