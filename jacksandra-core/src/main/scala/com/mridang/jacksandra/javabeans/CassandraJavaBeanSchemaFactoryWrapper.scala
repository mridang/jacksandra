package com.mridang.jacksandra.javabeans

import com.datastax.oss.driver.api.core.`type`.DataType
import com.fasterxml.jackson.databind.{JavaType, SerializerProvider}
import com.fasterxml.jackson.module.jsonSchema.factories.{ObjectVisitor, VisitorContext, WrapperFactory}
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra.CassandraSchemaFactoryWrapper

class CassandraJavaBeanSchemaFactoryWrapper
(
  provider: SerializerProvider,
  wrapperFactory: WrapperFactory,
  dataFn: JavaType => DataType
) extends CassandraSchemaFactoryWrapper(provider, wrapperFactory, dataFn) {

  override def visitor(
                        provider: SerializerProvider,
                        schema: ObjectSchema,
                        wrapperFactory: WrapperFactory,
                        visitorContext: VisitorContext): ObjectVisitor =
    new CassandraJavaBeanSchemaObjectVisitor(
      provider,
      schema,
      wrapperFactory,
      visitorContext)

  def cassandraVisitorContext: VisitorContext = {
    visitorContext
  }
}
