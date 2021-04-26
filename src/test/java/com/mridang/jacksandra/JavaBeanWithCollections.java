package com.mridang.jacksandra;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableList;
import com.datastax.oss.driver.shaded.guava.common.collect.ImmutableSet;
import com.mridang.jacksandra.types.FrozenList;
import com.mridang.jacksandra.types.FrozenSet;

/**
 *
 * tinyint	integers	1 byte integer
 * smallint	integers	2 byte integer
 * int	integers	32-bit signed integer
 * bigint	integers	64-bit signed long
 * varint	integers	Arbitrary-precision integer
 *
 * float	integers, floats	32-bit IEEE-754 floating point
 * double	integers, floats	64-bit IEEE-754 floating point
 * decimal	integers, floats	Variable-precision decimal
 *
 */
@SuppressWarnings("unused")
@CqlName("myjavabeanwithcollections")
public class JavaBeanWithCollections {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionKey")
    public String myPartitionKey;

    @Nullable
    @CqlName("toFrozenListString")
    public FrozenList<String> toFrozenListString;

    @Nullable
    @CqlName("toListString")
    public List<String> toListString;

    @Nullable
    @CqlName("toImmutableListString")
    public ImmutableList<String> toImmutableListString;

    @Nullable
    @CqlName("toSetListString")
    public FrozenSet<String> toSetListString;

    @Nullable
    @CqlName("toSetString")
    public Set<String> toSetString;

    @Nullable
    @CqlName("toImmutableSetString")
    public ImmutableSet<String> toImmutableSetString;

}
