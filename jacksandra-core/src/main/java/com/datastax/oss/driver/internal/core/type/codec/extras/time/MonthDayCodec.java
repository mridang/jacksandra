package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.MonthDay;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class MonthDayCodec extends MappingCodec<String, MonthDay> {

    public MonthDayCodec() {
        super(TypeCodecs.TEXT, GenericType.of(MonthDay.class));
    }

    @Override
    protected MonthDay innerToOuter(String value) {
        return value == null ? null : MonthDay.parse(value);
    }

    @Override
    protected String outerToInner(MonthDay value) {
        return value == null ? null : value.toString();
    }
}
