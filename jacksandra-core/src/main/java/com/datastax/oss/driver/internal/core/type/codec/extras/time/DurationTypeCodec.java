package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.nio.ByteBuffer;
import java.time.Duration;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.NonNull;

public class DurationTypeCodec implements TypeCodec<Duration> {

    @NonNull
    @Override
    public GenericType<Duration> getJavaType() {
        return GenericType.DURATION;
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.DURATION;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof Duration;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable Duration value, @NonNull ProtocolVersion protocolVersion) {
        return null;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == Duration.class;
    }

    @Nullable
    @Override
    public Duration decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return null;
    }

    @NonNull
    @Override
    public String format(@Nullable Duration value) {
        return (value == null) ? "NULL" : value.toString();
    }

    @Nullable
    @Override
    public Duration parse(@Nullable String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        }
        return Duration.parse(value);
    }
}
