package org.jeasy.random.randomizers.extras;

import java.math.BigDecimal;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@SuppressWarnings("unused")
@SuppressFBWarnings("NM_SAME_SIMPLE_NAME_AS_SUPERCLASS")
public class BigDecimalRandomizer extends org.jeasy.random.randomizers.number.BigDecimalRandomizer {

    @Override
    public BigDecimal getRandomValue() {
        return super.getRandomValue().stripTrailingZeros();
    }
}
