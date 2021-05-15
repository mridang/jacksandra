package com.mridang.jacksandra;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

import javax.annotation.Nullable;

import com.datastax.oss.driver.api.mapper.annotations.CqlName;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;

@SuppressWarnings("unused")
@CqlName("myjavabeanwithtemporals")
public class JavaBeanWithTemporal {

    @PartitionKey
    @Nullable
    @CqlName("mypartitionKey")
    public String myPartitionKey;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("toDateTimestamp")
    public Date toDateTimestamp;

    /**
     * {@link com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("toTimestampTimestamp")
    public Timestamp toTimestampTimestamp;

    /**
     * See com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("toInstantTimestamp")
    public Instant toInstantTimestamp;

    /**
     * Force shaping.
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.OffsetDateTimeSerializer}
     *
     */
    @Nullable
    @CqlName("toOffsetDateTimeString")
    public OffsetDateTime toOffsetDateTimeString;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer}
     * {@link com.datastax.oss.driver.internal.core.type.codec.extras.time.ZonedTimestampCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toZonedDateTimeString")
    public ZonedDateTime toZonedDateTimeString;

    /**
     * {@link com.datastax.oss.driver.api.core.data.CqlDuration}
     * {@link com.datastax.oss.driver.internal.core.type.codec.CqlDurationCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#DURATION}
     */
    @Nullable
    @CqlName("toJavaDurationDuration")
    public Duration toJavaDurationDuration;

    /**
     * Force shaping
     * {@link com.datastax.oss.driver.internal.core.type.codec.extras.time.LocalTimestampCodec}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIMESTAMP}
     */
    @Nullable
    @CqlName("toLocalDateTimeTimeStamp")
    public LocalDateTime toLocalDateTimeTimeStamp;

    /**
     * Force shaping
     * See com.datastax.oss.driver.internal.core.type.codec.DateCodec
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#DATE}
     */
    @Nullable
    @CqlName("toLocalDateDate")
    public LocalDate toLocalDateDate;

    /**
     * Force shaping
     * See com.datastax.oss.driver.internal.core.type.codec.TimeCodec
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TIME
     */
    @Nullable
    @CqlName("toLocalTimeTime")
    public LocalTime toLocalTimeTime;

    /**
     *
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.MonthDayDeserializer}
     * and {@link com.fasterxml.jackson.datatype.jsr310.ser.MonthDaySerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toMonthDayString")
    public MonthDay toMonthDayString;

    /**
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.OffsetTimeSerializer
     * and {@link com.fasterxml.jackson.datatype.jsr310.deser.OffsetTimeDeserializer
     */
    @Nullable
    @CqlName("toOffsetTimeString")
    public OffsetTime toOffsetTimeString;

    /**
     * {@link com.datastax.dse.driver.internal.core.type.codec.time.DateRangeCodec}
     * {@link com.datastax.dse.driver.api.core.type.DseDataTypes#DATE_RANGE}
     */
    @Nullable
    @CqlName("toPeriodDateRange")
    public Period toPeriodDateRange;

    /**
     * No shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.YearSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT
     */
    @Nullable
    @CqlName("toYearString")
    public Year toYearString;

    /**
     *
     * Force shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.YearMonthDeserializer
     * and {@link com.fasterxml.jackson.datatype.jsr310.ser.YearMonthSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toYearMonthString")
    public YearMonth toYearMonthString;

    /**
     * No shaping.
     * {@link com.fasterxml.jackson.datatype.jsr310.ser.ZoneIdSerializer}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toZoneIdString")
    public ZoneId toZoneIdString;

    /**
     * No shaping
     * {@link com.fasterxml.jackson.datatype.jsr310.deser.JSR310StringParsableDeserializer#ZONE_OFFSET}
     * {@link com.datastax.oss.driver.api.core.type.DataTypes#TEXT}
     */
    @Nullable
    @CqlName("toZoneOffsetString")
    public ZoneOffset toZoneOffsetString;
}
