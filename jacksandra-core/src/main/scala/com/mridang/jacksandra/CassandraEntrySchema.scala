package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
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
class CassandraEntrySchema(val javaType: JavaType, val keyDataType: DataType, val valueDataType: DataType)
  extends ObjectSchema with CassandraType {

  val isFrozen: Boolean = classOf[Frozen].isAssignableFrom(javaType.getRawClass)

  override def getDataType: DataType = {
    DataTypes.mapOf(keyDataType, valueDataType)
  }

  override def getJavaType: JavaType = javaType

}
