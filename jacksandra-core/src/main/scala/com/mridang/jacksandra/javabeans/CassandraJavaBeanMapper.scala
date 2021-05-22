package com.mridang.jacksandra.javabeans

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory
import com.mridang.jacksandra._

import scala.reflect.ClassTag

class CassandraJavaBeanMapper[T](keyspace: String, dataTypeFn: JavaType => DataType = CassandraMapper.getDT)(implicit classTag: ClassTag[T])
  extends CassandraMapper[T](keyspace = keyspace, dataFn = dataTypeFn) {

  override def wrapperFactory: WrapperFactory =
    new CassandraSchemaFactoryWrapperFactory((provider, factory) =>
      new CassandraSchemaFactoryWrapper(provider, factory, dataTypeFn))
}
