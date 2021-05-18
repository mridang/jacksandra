package com.datastax.oss.driver.internal.core.type.codec.extras;

import java.nio.ByteBuffer;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;
import com.datastax.oss.protocol.internal.util.Bytes;
import com.mridang.jacksandra.types.CqlBlob;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;

public class CqlBlobCodec implements TypeCodec<CqlBlob> {

    @NonNull
    @Override
    public GenericType<CqlBlob> getJavaType() {
        return GenericType.of(CqlBlob.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.BLOB;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof CqlBlob;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return CqlBlob.class.equals(javaClass);
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable CqlBlob value, @NonNull ProtocolVersion protocolVersion) {
        return (value == null) ? null : ByteBuffer.wrap(value.getBytes());
    }

    @Nullable
    @Override
    public CqlBlob decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return (bytes == null) ? null : new CqlBlob(bytes.duplicate().array());
    }

    @NonNull
    @Override
    public String format(@Nullable CqlBlob value) {
        return (value == null) ? "NULL" : value.toString();
    }

    @Nullable
    @Override
    public CqlBlob parse(@Nullable String value) {
        return (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL"))
                ? null
                : new CqlBlob(Bytes.fromHexString(value).array());
    }
}
