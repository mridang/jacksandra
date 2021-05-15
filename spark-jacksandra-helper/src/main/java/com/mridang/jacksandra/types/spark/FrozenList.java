package com.mridang.jacksandra.types.spark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A FrozenList implementation for usage in beans in Spark. As Spark requires
 * types to be serializable, this implementation simply extends the actual frozen
 * list along with the {@link java.io.Serializable}.
 *
 * When a bean requires a frozen list and that bean is used in Spark, you should
 * use this implementation over {@link com.mridang.jacksandra.types.FrozenList}
 *
 * See {@link com.mridang.jacksandra.types.FrozenList} for more information.
 *
 * @author mridang
 */
@SuppressWarnings("unused")
public class FrozenList<E> extends com.mridang.jacksandra.types.FrozenList<E> implements Serializable {

    @SuppressWarnings("unused")
    public FrozenList() {
        this(new ArrayList<>());
    }

    public FrozenList(List<E> backingList) {
        super(backingList);
    }
}
