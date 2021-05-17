package org.jeasy.random.randomizers.extras;

import org.jeasy.random.api.ContextAwareRandomizer;
import org.jeasy.random.api.RandomizerContext;

import com.google.common.collect.ImmutableSet;

public class ImmutableSetRandomizer implements ContextAwareRandomizer<ImmutableSet<?>> {

    private RandomizerContext context;

    @Override
    public void setRandomizerContext(RandomizerContext context) {
        this.context = context;
    }

    @Override
    public ImmutableSet<?> getRandomValue() {
        return ImmutableSet.of();
    }
}
