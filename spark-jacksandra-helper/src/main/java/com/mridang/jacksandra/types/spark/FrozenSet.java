package com.mridang.jacksandra.types.spark;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A FrozenSet implementation for usage in beans in Spark. As Spark requires
 * types to be serializable, this implementation simply extends the actual frozen
 * set along with the {@link java.io.Serializable}.
 *
 * When a bean requires a frozen set and that bean is used in Spark, you should
 * use this implementation over {@link com.mridang.jacksandra.types.FrozenSet}
 *
 * See {@link com.mridang.jacksandra.types.FrozenSet} for more information.
 *
 * @author mridang
 */
@SuppressWarnings("unused")
public class FrozenSet<E> extends com.mridang.jacksandra.types.FrozenSet<E> implements Serializable {

    @SuppressWarnings("unused")
    public FrozenSet() {
        this(new HashSet<>());
    }

    public FrozenSet(Set<E> backingSet) {
        super(backingSet);
    }
}
