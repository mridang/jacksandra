package org.jeasy.random.randomizers.extras;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.jeasy.random.randomizers.time.LocalTimeRandomizer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("unused")
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class LocalDateTimeRandomizer extends org.jeasy.random.randomizers.time.LocalDateTimeRandomizer {

    public LocalDateTimeRandomizer() {
        super();
        super.setLocalTimeRandomizer(new LocalTimeRandomizer() {
            @Override
            public LocalTime getRandomValue() {
                return super.getRandomValue().truncatedTo(ChronoUnit.MILLIS);
            }
        });
    }
}
