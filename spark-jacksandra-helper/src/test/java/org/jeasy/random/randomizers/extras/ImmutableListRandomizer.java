package org.jeasy.random.randomizers.extras;

import com.google.common.collect.ImmutableList;

public class ImmutableListRandomizer extends CollectionTypeRandomizer<ImmutableList<?>> {

    @SuppressWarnings("unused")
    public ImmutableListRandomizer() {
        super();
    }

    @Override
    public ImmutableList<?> getRandomValue(Object[] value) {
        return ImmutableList.copyOf(value);
    }
}
