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

import java.sql.Date;
import java.time.LocalDate;

import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class LocalDateCodec extends MappingCodec<LocalDate, Date> {

    public LocalDateCodec() {
        super(TypeCodecs.DATE, GenericType.of(Date.class));
    }

    @Override
    protected Date innerToOuter(LocalDate value) {
        return value == null ? null : Date.valueOf(value);
    }

    @Override
    protected LocalDate outerToInner(Date value) {
        return value == null ? null : value.toLocalDate();
    }
}
