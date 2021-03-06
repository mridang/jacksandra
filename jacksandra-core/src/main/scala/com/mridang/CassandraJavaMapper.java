package com.mridang;

import java.util.List;

import com.datastax.oss.driver.api.core.type.codec.registry.CodecRegistry;
import com.datastax.oss.driver.internal.core.type.codec.extras.CassandraCodecRegistry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;
import com.fasterxml.jackson.module.jsonSchema.factories.WrapperFactory;
import com.mridang.jacksandra.CassandraObjectMapper;
import com.mridang.jacksandra.CassandraSchema;
import com.mridang.jacksandra.CassandraSchemaFactoryWrapper;
import com.mridang.jacksandra.CassandraSchemaFactoryWrapperFactory;
import com.mridang.jacksandra.CodecProvider;
import com.mridang.jacksandra.RegistryBasedCodecProvider;

import scala.collection.JavaConverters;
import scala.runtime.AbstractFunction2;

@SuppressWarnings("unused")
public class CassandraJavaMapper<T> {

    private final WrapperFactory wrapperFactory;
    private final ObjectMapper schemaMapper = new CassandraObjectMapper();
    private final Class<?> mappedKlass;
    private final String defaultKeyspace;

    public CassandraJavaMapper(String defaultKeyspace, Class<T> mappedKlass) {
        this(defaultKeyspace, mappedKlass, new CassandraCodecRegistry());
    }

    public CassandraJavaMapper(String defaultKeyspace, Class<T> mappedKlass, CodecRegistry codecRegistry) {
        this(defaultKeyspace, mappedKlass, new RegistryBasedCodecProvider(codecRegistry));
    }

    public CassandraJavaMapper(String defaultKeyspace, Class<T> mappedKlass, CodecProvider codecProvider) {
        this.defaultKeyspace = defaultKeyspace;
        this.mappedKlass = mappedKlass;
        //noinspection Convert2Diamond
        this.wrapperFactory = new CassandraSchemaFactoryWrapperFactory(new AbstractFunction2<SerializerProvider, WrapperFactory, SchemaFactoryWrapper>() {
            @Override
            public SchemaFactoryWrapper apply(SerializerProvider serializerProvider, WrapperFactory wrapperFactory) {
                return new CassandraSchemaFactoryWrapper(serializerProvider, wrapperFactory, codecProvider);
            }
        });
    }

    public List<String> toSchema() {
        SchemaFactoryWrapper schemaFactoryWrapper = wrapperFactory.getWrapper(schemaMapper.getSerializerProvider());
        try {
            schemaMapper.acceptJsonFormatVisitor(mappedKlass, schemaFactoryWrapper);
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }

        return JavaConverters.seqAsJavaList(CassandraSchema.from(defaultKeyspace, schemaFactoryWrapper));
    }

    public String toJson(Object obj) throws JsonProcessingException {
        return schemaMapper.writeValueAsString(obj);
    }
}
