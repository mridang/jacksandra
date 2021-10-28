package com.datastax.oss.driver.internal.core.type.codec.extras.time;

import java.time.Duration;

import com.datastax.oss.driver.api.core.data.CqlDuration;
import com.datastax.oss.driver.api.core.type.codec.MappingCodec;
import com.datastax.oss.driver.api.core.type.codec.TypeCodecs;
import com.datastax.oss.driver.api.core.type.reflect.GenericType;

public class DurationTypeCodec extends MappingCodec<CqlDuration, Duration> {

    public DurationTypeCodec() {
        super(TypeCodecs.DURATION, GenericType.of(Duration.class));
    }

    @Override
    protected Duration innerToOuter(CqlDuration value) {
        return value == null ? null : Duration.parse(value.toString());
    }

    @Override
    protected CqlDuration outerToInner(Duration value) {
        return value == null ? null : CqlDuration.from(value.toString());
    }
}
