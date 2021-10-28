package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.nio.ByteBuffer;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.jacksandra.types.CqlBlob;

@SuppressWarnings("ByteBufferBackingArray")
public class CqlBlobCodec extends MappingCodec<ByteBuffer, CqlBlob> {

    public CqlBlobCodec() {
        super(TypeCodecs.BLOB, GenericType.of(CqlBlob.class));
    }

    @Override
    protected CqlBlob innerToOuter(ByteBuffer value) {
        return value == null ? null : new CqlBlob(value.array());
    }

    @Override
    protected ByteBuffer outerToInner(CqlBlob value) {
        return value == null ? null : ByteBuffer.wrap(value.getBytes());
    }
}

