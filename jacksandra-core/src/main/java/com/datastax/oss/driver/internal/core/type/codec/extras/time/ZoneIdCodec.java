package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.ZoneId;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.Nullable;

public class ZoneIdCodec extends MappingCodec<String, ZoneId> {

    public ZoneIdCodec() {
        super(TypeCodecs.TEXT, GenericType.of(ZoneId.class));
    }

    @Override
    protected ZoneId innerToOuter(String value) {
        return value == null ? null : ZoneId.of(value);
    }

    @Override
    protected String outerToInner(ZoneId value) {
        return value == null ? null : value.toString();
    }
}
