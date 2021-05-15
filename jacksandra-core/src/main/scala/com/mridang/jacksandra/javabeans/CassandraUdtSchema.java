package com.mridang.jacksandra.javabeans;

import java.util.Map;

import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

/**
 * Wraps the object-schema returned by Jackson and contains the deduced corresponding
 * user-defined type
 *
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cqlRefUDType.html
 *
 * @author mridang
 */
public class CassandraUdtSchema extends ObjectSchema implements CassandraSchema {

    private final JavaType javaType;
    public final ObjectSchema backing;
    private final DataType dataType;

    public CassandraUdtSchema(JavaType javaType, ObjectSchema backing, CqlName name) {
        this.javaType = javaType;
        this.backing = backing;
        this.dataType = QueryBuilder.udt(name != null ? name.value() : javaType.getRawClass().getSimpleName());
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public JavaType getJavaType() {
        return javaType;
    }

    @Override
    public Map<String, JsonSchema> getProperties() {
        return backing.getProperties();
    }
}
