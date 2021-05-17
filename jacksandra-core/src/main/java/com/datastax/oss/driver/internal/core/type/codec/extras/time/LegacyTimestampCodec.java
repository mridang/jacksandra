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
import java.sql.Timestamp;
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
public class LegacyTimestampCodec implements TypeCodec<Timestamp> {

    @NonNull
    @Override
    public GenericType<Timestamp> getJavaType() {
        return GenericType.of(Timestamp.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMESTAMP;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof Timestamp;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == Timestamp.class;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable Timestamp value, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(value)
                .map(val -> TypeCodecs.TIMESTAMP.encode(value.toInstant(), protocolVersion))
                .orElse(null);
    }

    @Nullable
    @Override
    public Timestamp decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.decode(bytes, protocolVersion))
                .map(Timestamp::from)
                .orElse(null);
    }

    @NonNull
    @Override
    public String format(@Nullable Timestamp value) {
        return (value == null) ? "NULL" : TypeCodecs.TIMESTAMP.format(value.toInstant());
    }

    @Nullable
    @Override
    public Timestamp parse(@Nullable String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        }
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.parse(value))
                .map(Timestamp::from)
                .orElse(null);
    }
}
