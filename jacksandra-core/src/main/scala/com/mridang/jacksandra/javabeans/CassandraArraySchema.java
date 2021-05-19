package com.mridang.jacksandra.javabeans;

import java.util.List;
import java.util.Set;

import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.module.jsonSchema.types.ArraySchema;
import com.mridang.jacksandra.types.Frozen;
import com.mridang.jacksandra.types.FrozenList;
import com.mridang.jacksandra.types.FrozenSet;

/**
 * Wraps the array-schema returned by Jackson and contains the deduced corresponding
 * collection type.
 * <p>
 * https://docs.datastax.com/en/cql-oss/3.3/cql/cql_reference/collection_type_r.html
 * <p>
 * If a frozen collection type is needed, you must use {@link FrozenList}
 * or {@link FrozenSet}. if those don't suit the needs, any collection
 * type can be used so long as it implements the {@link Frozen}
 * interface.
 *
 * @author mridang
 */
public class CassandraArraySchema extends ArraySchema implements CassandraSchema {

    private final JavaType javaType;
    private final DataType dataType;

    public CassandraArraySchema(JavaType javaType, DataType dataType) {
        this.javaType = javaType;
        boolean isFrozen = Frozen.class.isAssignableFrom(javaType.getRawClass());
        if (List.class.isAssignableFrom(javaType.getRawClass())) {
            if (isFrozen) {
                this.dataType = DataTypes.frozenListOf(dataType);
            } else {
                this.dataType = DataTypes.listOf(dataType);
            }
        } else if (Set.class.isAssignableFrom(javaType.getRawClass())) {
            if (isFrozen) {
                this.dataType = DataTypes.frozenSetOf(dataType);
            } else {
                this.dataType = DataTypes.setOf(dataType);
            }
        } else {
            //TODO: Scala collections should be handled here.
            //throw new IllegalStateException("Unexpected array type " + javaType.getRawClass());
            this.dataType = DataTypes.listOf(dataType);
        }
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
