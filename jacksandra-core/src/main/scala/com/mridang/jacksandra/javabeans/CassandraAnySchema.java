package com.mridang.jacksandra.javabeans;

import com.datastax.oss.driver.api.core.type.DataType;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.types.AnySchema;

/**
 * Wraps the any-schema returned by Jackson and contains the deduced corresponding
 * type.
 *
 * This is used rarely as I'm not sure what comprises any "any" type.
 *
 * @author mridang
 */
public class CassandraAnySchema extends AnySchema implements CassandraSchema {

    private final JavaType javaType;
    private final DataType dataType;

    public CassandraAnySchema(JavaType javaType, DataType dataType) {
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
