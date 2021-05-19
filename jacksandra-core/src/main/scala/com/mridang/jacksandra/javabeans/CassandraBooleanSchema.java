package com.mridang.jacksandra.javabeans;

import com.datastax.oss.driver.api.core.type.DataType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.types.BooleanSchema;

/**
 * Wraps the boolean-schema returned by Jackson and contains the deduced corresponding
 * boolean type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cql_data_types_c.html
 *
 * @author mridang
 */
public class CassandraBooleanSchema extends BooleanSchema implements CassandraSchema {

    private final JavaType javaType;
    private final DataType dataType;

    public CassandraBooleanSchema(JavaType javaType, DataType dataType) {
        this.javaType = javaType;
        this.dataType = dataType;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public JavaType getJavaType() {
        return javaType;
    }
}
