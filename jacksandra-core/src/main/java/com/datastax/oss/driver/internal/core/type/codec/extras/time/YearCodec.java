package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.Year;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class YearCodec extends MappingCodec<Integer, Year> {

    public YearCodec() {
        super(TypeCodecs.INT, GenericType.of(Year.class));
    }

    @Override
    protected Year innerToOuter(Integer value) {
        return value == null ? null : Year.parse(value.toString());
    }

    @Override
    protected Integer outerToInner(Year value) {
        return value == null ? null : Integer.valueOf(value.toString());
    }
}
