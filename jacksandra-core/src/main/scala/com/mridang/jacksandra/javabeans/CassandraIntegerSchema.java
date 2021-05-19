package com.mridang.jacksandra.javabeans;

import com.datastax.oss.driver.api.core.type.DataType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.types.IntegerSchema;

/**
 * Wraps the integer-schema returned by Jackson and contains the deduced corresponding
 * numeric type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cql_data_types_c.html
 *
 * @author mridang
 */
public class CassandraIntegerSchema extends IntegerSchema implements CassandraSchema {

    private final JavaType javaType;
    private final DataType dataType;

    public CassandraIntegerSchema(JavaType javaType, DataType dataType) {
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
