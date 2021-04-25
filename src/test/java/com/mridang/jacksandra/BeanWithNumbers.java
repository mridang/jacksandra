package com.mridang.jacksandra;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

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
@CqlName("mynumberbean")
public class BeanWithNumbers {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionKey")
    public String myPartitionKey;

    @Nullable
    @CqlName("toTinyInt")
    public Byte toTinyInt;

    @Nullable
    @CqlName("toSmallInt")
    public Short toSmallInt;

    @Nullable
    @CqlName("toInt")
    public Integer toInt;

    @Nullable
    @CqlName("toBigInt")
    public Long toBigInt;

    @Nullable
    @CqlName("toVarInt")
    public BigInteger toVarInt;

    @Nullable
    @CqlName("toFloat")
    public Float toFloat;

    @Nullable
    @CqlName("toDouble")
    public Double toDouble;

    @Nullable
    @CqlName("toBigDecimal")
    public BigDecimal toBigDecimal;

}
