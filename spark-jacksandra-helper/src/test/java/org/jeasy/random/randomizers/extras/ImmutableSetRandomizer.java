package org.jeasy.random.randomizers.extras;

import com.google.common.collect.ImmutableSet;

public class ImmutableSetRandomizer extends CollectionTypeRandomizer<ImmutableSet<?>> {

    @SuppressWarnings("unused")
    public ImmutableSetRandomizer() {
        super();
    }

    @Override
    public ImmutableSet<?> getRandomValue(Object[] value) {
        return ImmutableSet.copyOf(value);
    }
}
