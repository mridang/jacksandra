package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.util.UUID;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.mridang.jacksandra.types.CqlTimeUUID;

public class CqlTimeUUIDCodec extends MappingCodec<UUID, CqlTimeUUID> {

    public CqlTimeUUIDCodec() {
        super(TypeCodecs.TIMEUUID, GenericType.of(CqlTimeUUID.class));
    }

    @Override
    protected CqlTimeUUID innerToOuter(UUID value) {
        return value == null ? null : new CqlTimeUUID(value);
    }

    @Override
    protected UUID outerToInner(CqlTimeUUID value) {
        return value == null ? null : value.getUUID();
    }
}
