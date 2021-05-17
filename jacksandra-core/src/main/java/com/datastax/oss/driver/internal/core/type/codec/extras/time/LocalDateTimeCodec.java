/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import com.datastax.oss.driver.api.core.ProtocolVersion;
import com.datastax.oss.driver.api.core.type.DataType;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.core.type.codec.TypeCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

import edu.umd.cs.findbugs.annotations.NonNull;
import edu.umd.cs.findbugs.annotations.Nullable;
import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class LocalDateTimeCodec implements TypeCodec<LocalDateTime> {

    @NonNull
    @Override
    public GenericType<LocalDateTime> getJavaType() {
        return GenericType.of(LocalDateTime.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMESTAMP;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof LocalDateTime;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == LocalDateTime.class;
    }

    @Nullable
    @Override
    @SuppressWarnings("FromTemporalAccessor")
    public ByteBuffer encode(@Nullable LocalDateTime value, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(value)
                .map(val -> TypeCodecs.TIMESTAMP.encode(Instant.from(value), protocolVersion))
                .orElse(null);
    }

    @Nullable
    @Override
    public LocalDateTime decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.decode(bytes, protocolVersion))
                .map(LocalDateTime::from)
                .orElse(null);
    }

    @NonNull
    @Override
    @SuppressWarnings("FromTemporalAccessor")
    public String format(@Nullable LocalDateTime value) {
        return (value == null) ? "NULL" : TypeCodecs.TIMESTAMP.format(Instant.from(value));
    }

    @Nullable
    @Override
    public LocalDateTime parse(@Nullable String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        }
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.parse(value))
                .map(LocalDateTime::from)
                .orElse(null);
    }
}
