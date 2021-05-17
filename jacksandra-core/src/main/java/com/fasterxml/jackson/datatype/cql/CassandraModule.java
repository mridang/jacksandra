package com.fasterxml.jackson.datatype.cql;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.cql.deser.DateDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.DurationDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.cql.deser.TimestampDeserializer;
import com.fasterxml.jackson.datatype.cql.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jdk8.PackageVersion;

public class CassandraModule extends SimpleModule {

    public CassandraModule() {
        super(PackageVersion.VERSION);
        addDeserializer(Date.class, new DateDeserializer());
        addDeserializer(Duration.class, new DurationDeserializer());
        addDeserializer(Instant.class, new InstantDeserializer());
        addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer());
        addDeserializer(Timestamp.class, new TimestampDeserializer());
        addSerializer(LocalDateTime.class, new LocalDateTimeSerializer());
    }

    @Override
    public String getModuleName() {
        return "CassandraModule";
    }
}
