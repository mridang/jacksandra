package com.datastax.oss.driver.internal.core.type.codec.extras;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.jacksandra.types.CqlAscii;

public class CqlAsciiCodec extends MappingCodec<String, CqlAscii> {

    public CqlAsciiCodec() {
        super(TypeCodecs.ASCII, GenericType.of(CqlAscii.class));
    }

    @Override
    protected CqlAscii innerToOuter(String value) {
        return value == null ? null : new CqlAscii(value);
    }

    @Override
    protected String outerToInner(CqlAscii value) {
        return value == null ? null : value.getString();
    }
}
