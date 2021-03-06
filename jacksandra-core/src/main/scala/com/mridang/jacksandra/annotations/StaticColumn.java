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
package com.mridang.jacksandra.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.Select;

/**
 * Annotates the field or getter of an {@link Entity} property, to indicate that it's a static
 * column.
 *
 * <p>Example:
 *
 * <pre>
 * &#64;StaticColumn private int month;
 * </pre>
 * <p>
 * This information is used by the mapper processor to generate default queries (for example a basic
 * {@link Select}).
 *
 * <p>If there are multiple clustering columns, you simply annotate each property:
 *
 * <pre>
 * &#64;StaticColumn private int month;
 * &#64;StaticColumn private int day;
 * </pre>
 *
 * <p>This annotation is mutually exclusive with {@link PartitionKey} and
 * {@link ClusteringColumn}.
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@SuppressWarnings("unused")
public @interface StaticColumn {
}
