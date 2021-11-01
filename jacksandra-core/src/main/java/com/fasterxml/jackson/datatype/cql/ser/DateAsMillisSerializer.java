package com.fasterxml.jackson.datatype.cql.ser;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

@SuppressWarnings({"JavaUtilDate", "unused"})
public class DateAsMillisSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider serializers)
            throws IOException {

        jsonGenerator.writeNumber(value.getTime());
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType type) throws JsonMappingException {
        visitor.expectNumberFormat(type);
    }
}
