package org.jeasy.random.randomizers.extras;

import org.jeasy.random.api.ContextAwareRandomizer;
import org.jeasy.random.api.RandomizerContext;

import com.google.common.collect.ImmutableList;

public class ImmutableListRandomizer implements ContextAwareRandomizer<ImmutableList<?>> {

    private RandomizerContext context;

    @Override
    public void setRandomizerContext(RandomizerContext context) {
        this.context = context;
    }

    @Override
    public ImmutableList<?> getRandomValue() {
        try {
            System.out.println(context.getTargetType().getField(context.getCurrentField()));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ImmutableList.of();
    }
}
