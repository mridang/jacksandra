package com.mridang.jacksandra.javabeans;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.types.ObjectSchema;

/**
 * Wraps the object-schema returned by Jackson and contains the deduced corresponding
 * user-defined type
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/cqlRefUDType.html
 *
 * @author mridang
 */
public class CassandraRootSchema extends CassandraUdtSchema {

    private final CqlName name;

    public CassandraRootSchema(JavaType javaType, ObjectSchema backing, CqlName name) {
        super(javaType, backing, name);
        this.name = name;
    }

    public String getTableName() {
        return name.value();
    }
}
