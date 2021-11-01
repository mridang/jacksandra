package org.jeasy.random.randomizers.extras;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class InstantRandomizer extends org.jeasy.random.randomizers.time.InstantRandomizer {

    @Override
    public Instant getRandomValue() {
        return super.getRandomValue().truncatedTo(ChronoUnit.MILLIS);
    }
}
