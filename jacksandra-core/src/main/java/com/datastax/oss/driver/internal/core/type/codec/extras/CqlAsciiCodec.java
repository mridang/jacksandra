package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.driver.internal.core.util.Strings;
import com.datastax.oss.protocol.internal.util.Bytes;
import com.mridang.jacksandra.types.CqlAscii;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public class CqlAsciiCodec implements TypeCodec<CqlAscii> {

    @NonNull
    @Override
    public GenericType<CqlAscii> getJavaType() {
        return GenericType.of(CqlAscii.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.ASCII;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof CqlAscii;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == CqlAscii.class;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable CqlAscii value, @NonNull ProtocolVersion protocolVersion) {
        return (value == null) ? null : ByteBuffer.wrap(value.getString().getBytes());
    }

    @Nullable
    @Override
    public CqlAscii decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        if (bytes == null) {
            return null;
        } else if (bytes.remaining() == 0) {
            return new CqlAscii("");
        } else {
            return new CqlAscii(new String(Bytes.getArray(bytes), StandardCharsets.US_ASCII));
        }
    }

    @NonNull
    @Override
    public String format(@Nullable CqlAscii value) {
        return (value == null) ? "NULL" : Strings.quote(value.getString());
    }

    @Nullable
    @Override
    public CqlAscii parse(String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        } else if (!Strings.isQuoted(value)) {
            throw new IllegalArgumentException(
                    "text or varchar values must be enclosed by single quotes");
        } else {
            return new CqlAscii(Strings.unquote(value));
        }
    }
}
