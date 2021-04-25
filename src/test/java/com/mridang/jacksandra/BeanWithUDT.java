package com.mridang.jacksandra;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.mridang.jacksandra.types.FrozenList;
import com.mridang.jacksandra.types.FrozenSet;

@SuppressWarnings("unused")
@Entity(defaultKeyspace = "mykeyspace")
public class BeanWithUDT {

    @Nullable
    @PartitionKey
    @CqlName("mypartitionKey")
    public String myPartitionKey;

    @Nullable
    @CqlName("toUdt")
    public SomeUDT toUdt;

    @Nullable
    @CqlName("toUdtList")
    public List<SomeUDT> toUdtList;

    @CqlName("myudt")
    public static class SomeUDT {

        @Nullable
        @CqlName("someString")
        public String someString;

        @Nullable
        @CqlName("someDouble")
        public Double someDouble;

        @Nullable
        @CqlName("someIntegerList")
        public List<Integer> someIntegerList;

        @Nullable
        @CqlName("someLongList")
        public FrozenList<Long> someLongList;

        @Nullable
        @CqlName("someFloatSet")
        public Set<Float> someFloatSet;

        @Nullable
        @CqlName("someDoubleSet")
        public FrozenSet<Double> someDoubleSet;

    }
}
