package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema
import com.mridang.jacksandra.types.Frozen

/**
 * Wraps the array-schema returned by Jackson and contains the deduced corresponding
 * collection type.
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/collection_type_r.html
 * <p>
 * If a frozen collection type is needed, you must use `FrozenList`
 * or `FrozenSet`. if those don't suit the needs, any collection
 * type can be used so long as it implements the `Frozen`
 * interface.
 *
 * @author mridang
 */
class CassandraArraySchema(val javaType: JavaType, val dataType: DataType)
  extends ArraySchema with CassandraType {

  val isFrozen: Boolean = classOf[Frozen].isAssignableFrom(javaType.getRawClass)

  //noinspection SimplifyBooleanMatch
  override def getDataType: DataType = {
    javaType.getRawClass match {
      case x if classOf[java.util.List[Any]].isAssignableFrom(x) =>
        isFrozen match {
          case true => DataTypes.frozenListOf(dataType)
          case false => DataTypes.listOf(dataType)
        }
      case x if classOf[java.util.Set[Any]].isAssignableFrom(x) =>
        isFrozen match {
          case true => DataTypes.frozenSetOf(dataType)
          case false => DataTypes.setOf(dataType)
        }
      case _ =>
        //TODO: Scala collections should be handled here.
        DataTypes.listOf(dataType)
    }
  }

  override def getJavaType: JavaType = javaType
}
