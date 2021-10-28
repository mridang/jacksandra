package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.ZoneOffset;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class ZoneOffsetCodec extends MappingCodec<String, ZoneOffset> {

    public ZoneOffsetCodec() {
        super(TypeCodecs.TEXT, GenericType.of(ZoneOffset.class));
    }

    @Override
    protected ZoneOffset innerToOuter(String value) {
        return value == null ? null : ZoneOffset.of(value);
    }

    @Override
    protected String outerToInner(ZoneOffset value) {
        return value == null ? null : value.toString();
    }
}
