package com.mridang.jacksandra

import com.datastax.oss.driver.api.core.`type`.reflect.GenericType
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.querybuilder.QueryBuilder
import com.datastax.oss.driver.internal.core.`type`.codec.registry.DefaultCodecRegistry
import com.fasterxml.jackson.databind.`type`.CollectionType
import com.fasterxml.jackson.databind.jsonFormatVisitors._
import com.fasterxml.jackson.databind.{JavaType, SerializerProvider}
import com.fasterxml.jackson.module.jsonSchema.factories.{
  ObjectVisitor,
  SchemaFactoryWrapper,
  VisitorContext,
  WrapperFactory
}
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema
import com.mridang.jacksandra.javabeans._

/**
  * A customized visitor that intercepts generated [[JsonSchema]] instances
  * and uses [[CassandraJsonSchemaBase]] based objects instead.
  */
class CassandraSchemaFactoryWrapper(
    _provider: SerializerProvider,
    /* use only as an initial value!! */
    _wrapperFactory: WrapperFactory)
    extends SchemaFactoryWrapper(_provider, _wrapperFactory) {

  override def expectAnyFormat(
      convertedType: JavaType): JsonAnyFormatVisitor = {
    val s = new CassandraAnySchema(convertedType, getDT(convertedType))
    this.schema = s
    visitorFactory.anyFormatVisitor(s)
  }

  /**
    * Returns a visitor when the bean properties are being traversed and and an
    * collection-like is found.
    *
    * Since Cassandra supports more tha just integers, we return a custom
    * `JsonSchema` that will contain the correct `DataType`.
    *
    * <ul>
    * <li>`DataTypes.listOf`</li>
    *
    * <ul>
    */
  override def expectArrayFormat(
      convertedType: JavaType): JsonArrayFormatVisitor = {
    val jsonFormatVisitor: JsonArrayFormatVisitor =
      super.expectArrayFormat(convertedType)
    this.schema = new CassandraArraySchema(
      convertedType,
      getDT(convertedType.asInstanceOf[CollectionType].getContentType))
    jsonFormatVisitor
  }

  /**
    * Returns a visitor when the bean properties are being traversed and and an
    * boolean is found.
    *
    * Since Cassandra supports more tha just integers, we return a custom
    * `JsonSchema` that will contain the correct `DataType`.
    *
    * <ul>
    * <li>`DataTypes.BOOLEAN`</li>
    * <ul>
    */
  override def expectBooleanFormat(
      convertedType: JavaType): JsonBooleanFormatVisitor = {
    val s = new CassandraBooleanSchema(convertedType, getDT(convertedType))
    this.schema = s
    visitorFactory.booleanFormatVisitor(s)
  }

  /**
    * Returns a visitor when the bean properties are being traversed and and an
    * integer is found.
    *
    * Since Cassandra supports more tha just integers, we return a custom
    * `JsonSchema` that will contain the correct `DataType`.
    *
    * <ul>
    * <li>`DataTypes.BIGINT`</li>
    * <li>`DataTypes.DECIMAL`</li>
    * <li>`DataTypes.DOUBLE`</li>
    * <li>`DataTypes.FLOAT`</li>
    * <li>`DataTypes.INT`</li>
    * <li>`DataTypes.SMALLINT`</li>
    * <li>`DataTypes.TINYINT`</li>
    * <li>`DataTypes.VARINT`</li>
    * <ul>
    */
  override def expectIntegerFormat(
      convertedType: JavaType): JsonIntegerFormatVisitor = {
    val s = new CassandraIntegerSchema(convertedType, getDT(convertedType))
    this.schema = s
    visitorFactory.integerFormatVisitor(s)
  }

  override def expectNullFormat(
      convertedType: JavaType): JsonNullFormatVisitor = {
    val s = schemaProvider.nullSchema
    schema = s
    visitorFactory.nullFormatVisitor(s)
  }

  /**
    * Returns a visitor when the bean properties are being traversed and and an
    * integer is found.
    *
    * Since Cassandra supports more tha just integers, we return a custom
    * `JsonSchema` that will contain the correct `DataType`.
    *
    * <ul>
    * <li>`DataTypes.BIGINT`</li>
    * <li>`DataTypes.DECIMAL`</li>
    * <li>`DataTypes.DOUBLE`</li>
    * <li>`DataTypes.FLOAT`</li>
    * <li>`DataTypes.INT`</li>
    * <li>`DataTypes.SMALLINT`</li>
    * <li>`DataTypes.TINYINT`</li>
    * <li>`DataTypes.VARINT`</li>
    * <ul>
    */
  override def expectNumberFormat(
      convertedType: JavaType): JsonNumberFormatVisitor = {
    val s = new CassandraNumberSchema(convertedType, getDT(convertedType))
    schema = s
    visitorFactory.numberFormatVisitor(s)
  }

  /**
    * Returns a visitor when the bean properties are being traversed and and an
    * string-like field is found.
    *
    * Since Cassandra supports more tha just just string, we return a custom
    * `JsonSchema` that will contain the correct `DataType`.
    *
    * <ul>
    * <li>`DataTypes.ASCII`</li>
    * <li>`DataTypes.TEXT`</li>
    * <li>`DataTypes.VARCHAR`</li>
    * <ul>
    */
  override def expectStringFormat(
      convertedType: JavaType): JsonStringFormatVisitor = {
    val s = new CassandraStringSchema(convertedType, getDT(convertedType))
    schema = s
    visitorFactory.stringFormatVisitor(s)
  }

  def getDT(javaType: JavaType): DataType = {
    val xx: Class[_] = javaType.getRawClass
    val udtName = Option(xx.getAnnotation(classOf[CqlName]))
    udtName match {
      case Some(name) => QueryBuilder.udt(name.value())
      case None => {
        try {
          val gt: GenericType[_] = GenericType.of(xx)
          val xxxxx = new DefaultCodecRegistry("mridang").codecFor(gt)
          xxxxx.getCqlType
        } catch {
          case e: Exception => {
            println("Had an IOException trying to read that file" + e)
          }
          DataTypes.TEXT
        }
      }
    }
  }

  override def expectMapFormat(`type`: JavaType): JsonMapFormatVisitor = {
    super.expectMapFormat(`type`)
  }

  /**
    * Customised [[ObjectSchema]] visits:
    * - Disable reference schemas as there is no support for such things in Cassandra (so don't call visitorContext.addSeenSchemaUri)
    * - Put [[CassandraJsonSchemaBase]] based objects to schema instead of standard [[JsonSchema]] ones.
    */
  override def expectObjectFormat(
      convertedType: JavaType): JsonObjectFormatVisitor = {
    // if we don't already have a recursive visitor context, create one
    if (visitorContext == null) visitorContext = new CassandraVisitorContext

    val s: ObjectSchema = schemaProvider.objectSchema
    if (CassandraSchemaFactoryWrapper.outerMost) {
      val name = convertedType.getRawClass.getAnnotation(classOf[CqlName])
      schema = new CassandraRootSchema(convertedType, s, name)
      CassandraSchemaFactoryWrapper.outerMost = false
    } else {
      val name = convertedType.getRawClass.getAnnotation(classOf[CqlName])
      val udtSchema = new CassandraUdtSchema(convertedType, s, name)
      schema = new CassandraItemSchema(udtSchema)
      visitorContext
        .asInstanceOf[CassandraVisitorContext]
        .addSeenUDT(name, udtSchema)
    }

    visitor(provider, s, _wrapperFactory, visitorContext)
  }

  def visitor(
      provider: SerializerProvider,
      schema: ObjectSchema,
      wrapperFactory: WrapperFactory,
      visitorContext: VisitorContext): ObjectVisitor =
    new CassandraSchemaObjectVisitor(
      provider,
      schema,
      wrapperFactory,
      visitorContext)
}

object CassandraSchemaFactoryWrapper {

  var outerMost: Boolean = true

}
