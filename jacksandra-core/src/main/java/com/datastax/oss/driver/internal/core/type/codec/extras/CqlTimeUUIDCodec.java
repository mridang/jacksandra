package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.nio.ByteBuffer;
import java.util.Optional;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.internal.core.util.Strings;
import com.mridang.jacksandra.types.CqlTimeUUID;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public class CqlTimeUUIDCodec implements TypeCodec<CqlTimeUUID> {

    @NonNull
    @Override
    public GenericType<CqlTimeUUID> getJavaType() {
        return GenericType.of(CqlTimeUUID.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMEUUID;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof CqlTimeUUID;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == CqlTimeUUID.class;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable CqlTimeUUID value, @NonNull ProtocolVersion protocolVersion) {
        if (value == null) {
            return null;
        } else if (value.getUUID().version() != 1) {
            throw new IllegalArgumentException(
                    String.format("%s is not a Type 1 (time-based) UUID", value));
        } else {
            return TypeCodecs.TIMEUUID.encode(value.getUUID(), protocolVersion);
        }
    }

    @Nullable
    @Override
    public CqlTimeUUID decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(TypeCodecs.TIMEUUID.decode(bytes, protocolVersion))
                .map(CqlTimeUUID::new)
                .orElse(null);
    }

    @NonNull
    @Override
    public String format(@Nullable CqlTimeUUID value) {
        if (value == null) {
            return "NULL";
        } else if (value.getUUID().version() != 1) {
            throw new IllegalArgumentException(
                    String.format("%s is not a Type 1 (time-based) UUID", value));
        } else {
            return TypeCodecs.TIMEUUID.format(value.getUUID());
        }
    }

    @Nullable
    @Override
    public CqlTimeUUID parse(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        } else if (!Strings.isQuoted(value)) {
            throw new IllegalArgumentException(
                    "text or varchar values must be enclosed by single quotes");
        } else {
            return new CqlTimeUUID(TypeCodecs.TIMEUUID.parse(value));
        }
    }
}
