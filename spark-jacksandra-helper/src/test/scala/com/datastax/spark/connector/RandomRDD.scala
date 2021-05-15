package com.datastax.spark.connector

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.jeasy.random.EasyRandom
import spire.ClassTag

/**
 * A RDD factory that uses Objeneis under the hood to inflate N number of
 * random items and return a pseudo RDD from it.
 *
 * An explanation of EasyRandom works is outside the scope of this. Refer to
 * https://github.com/j-easy/easy-random
 *
 * @param sc the instance of the Spark context
 * @param ctag an implicit class-tag reference as runtime evidence
 * @tparam T the type of entity to generate
 * @author mridang
 */
case class RandomRDD[T](sc: SparkContext)(implicit ctag: ClassTag[T]) {

  final val easyRandom: EasyRandom = new EasyRandom

  def item: T = {
    easyRandom.nextObject(ctag.runtimeClass.asInstanceOf[Class[T]])
  }

  /**
   * Generates an RDD of N items by inflating and hydrating random objects
   *
   * @param numItems the number of items to inflate.
   * @return An RDD of a random objects of size `numItems`
   */
  def of(numItems: Int): RDD[T] = {
    sc.parallelize(List.fill(numItems)(item))
  }
}
