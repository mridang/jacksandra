package com.datastax.spark.connector

import com.google.common.collect.{ImmutableList, ImmutableSet}
import com.mridang.jacksandra.types.{CqlAscii, CqlDuration, CqlTimeUUID}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.jeasy.random.randomizers.extras.{ByteBufferRandomizer, CqTimeUUIDRandomizer, CqlAsciiRandomizer, CqlDurationRandomizer, ImmutableListRandomizer, ImmutableSetRandomizer, InetAddressRandomizer, InstantRandomizer, ListRandomizer, LocalDateTimeRandomizer, SetRandomizer}
import org.jeasy.random.randomizers.number.BigDecimalRandomizer
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry
import org.jeasy.random.{EasyRandom, EasyRandomParameters}
import spire.ClassTag

import java.math.BigDecimal
import java.net.{Inet4Address, InetAddress}
import java.nio.ByteBuffer
import java.time.{Instant, LocalDateTime}

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

  final val easyRandom: EasyRandom = {
    val randomiserList: CustomRandomizerRegistry = new CustomRandomizerRegistry()
    randomiserList.registerRandomizer(classOf[ImmutableList[_]], new ImmutableListRandomizer())
    randomiserList.registerRandomizer(classOf[ImmutableSet[_]], new ImmutableSetRandomizer())
    randomiserList.registerRandomizer(classOf[BigDecimal], new BigDecimalRandomizer(Integer.valueOf(4)))
    randomiserList.registerRandomizer(classOf[CqlDuration], new CqlDurationRandomizer())
    randomiserList.registerRandomizer(classOf[CqlTimeUUID], new CqTimeUUIDRandomizer())
    randomiserList.registerRandomizer(classOf[CqlAscii], new CqlAsciiRandomizer())
    randomiserList.registerRandomizer(classOf[InetAddress], new InetAddressRandomizer())
    randomiserList.registerRandomizer(classOf[Inet4Address], new InetAddressRandomizer())
    randomiserList.registerRandomizer(classOf[LocalDateTime], new LocalDateTimeRandomizer())
    randomiserList.registerRandomizer(classOf[Instant], new InstantRandomizer())
    randomiserList.registerRandomizer(classOf[ByteBuffer], new ByteBufferRandomizer())
    randomiserList.registerRandomizer(classOf[scala.collection.immutable.List[_]], new ListRandomizer())
    randomiserList.registerRandomizer(classOf[scala.collection.immutable.Set[_]], new SetRandomizer())
    val randomParams = new EasyRandomParameters()
    randomParams.randomizerRegistry(randomiserList)
    new EasyRandom(randomParams)
  }
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
