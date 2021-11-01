package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@SuppressWarnings("unused")
public class InstantAsMillisSerializer extends JsonSerializer<Instant> {

    @Override
    public void serialize(Instant value, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {

        jsonGenerator.writeNumber(value.toEpochMilli()); // Jackson won't invoke this serializer if value is null
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        visitor.expectNumberFormat(type);
    }
}
