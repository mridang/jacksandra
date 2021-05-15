package com.datastax.spark.connector

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.language.implicitConversions

package object plus {

  implicit def toRDDFunctions[T](rdd: RDD[T]): CassandraRDDFunctions[T] =
    new CassandraRDDFunctions[T](rdd)

  implicit def toSparkContextFunctions(sc: SparkContext): JacksandraSparkContextFunctions =
    new JacksandraSparkContextFunctions(sc)
}
