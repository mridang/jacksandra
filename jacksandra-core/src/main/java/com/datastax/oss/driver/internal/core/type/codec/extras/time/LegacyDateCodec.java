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
import java.util.Date;
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
public class LegacyDateCodec implements TypeCodec<Date> {

    @NonNull
    @Override
    public GenericType<Date> getJavaType() {
        return GenericType.of(Date.class);
    }

    @NonNull
    @Override
    public DataType getCqlType() {
        return DataTypes.TIMESTAMP;
    }

    @Override
    public boolean accepts(@NonNull Object value) {
        return value instanceof Date;
    }

    @Override
    public boolean accepts(@NonNull Class<?> javaClass) {
        return javaClass == Date.class;
    }

    @Nullable
    @Override
    public ByteBuffer encode(@Nullable Date value, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(value)
                .map(val -> TypeCodecs.TIMESTAMP.encode(value.toInstant(), protocolVersion))
                .orElse(null);
    }

    @Nullable
    @Override
    public Date decode(@Nullable ByteBuffer bytes, @NonNull ProtocolVersion protocolVersion) {
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.decode(bytes, protocolVersion))
                .map(Date::from)
                .orElse(null);
    }

    @NonNull
    @Override
    public String format(@Nullable Date value) {
        return (value == null) ? "NULL" : TypeCodecs.TIMESTAMP.format(value.toInstant());
    }

    @Nullable
    @Override
    public Date parse(@Nullable String value) {
        if (value == null || value.isEmpty() || value.equalsIgnoreCase("NULL")) {
            return null;
        }
        return Optional.ofNullable(TypeCodecs.TIMESTAMP.parse(value))
                .map(Date::from)
                .orElse(null);
    }
}
