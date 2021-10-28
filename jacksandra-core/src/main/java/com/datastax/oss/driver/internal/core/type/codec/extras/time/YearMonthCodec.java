package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.YearMonth;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class YearMonthCodec extends MappingCodec<String, YearMonth> {

    public YearMonthCodec() {
        super(TypeCodecs.TEXT, GenericType.of(YearMonth.class));
    }

    @Override
    protected YearMonth innerToOuter(String value) {
        return value == null ? null : YearMonth.parse(value);
    }

    @Override
    protected String outerToInner(YearMonth value) {
        return value == null ? null : value.toString();
    }
}
