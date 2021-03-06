package org.jeasy.random.randomizers.extras;

import org.jeasy.random.api.Randomizer;
import org.jeasy.random.randomizers.time.DurationRandomizer;

import com.mridang.jacksandra.types.CqlDuration;

@SuppressWarnings("unused")
public class CqlDurationRandomizer implements Randomizer<CqlDuration> {

    private final DurationRandomizer durationRandomizer;

    public CqlDurationRandomizer() {
        this(new DurationRandomizer());
    }

    public CqlDurationRandomizer(DurationRandomizer durationRandomizer) {
        this.durationRandomizer = durationRandomizer;
    }

    @Override
    public CqlDuration getRandomValue() {
        return CqlDuration.from(durationRandomizer.getRandomValue().toString());
    }
}
