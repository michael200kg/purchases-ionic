package com.michael200kg.purchases;

import com.michael200kg.purchases.mongo.converters.NoteReadConverter;
import com.michael200kg.purchases.mongo.converters.NoteWriteConverter;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mikhail_Vershkov on 1/12/2019.
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String hostName;

    @Value("${spring.data.mongodb.port}")
    private Integer port;

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return this.database;
    }

    @Bean
    @Override
    public CustomConversions customConversions() {
        //List<Converter> converters = new ArrayList<>();
        List<Converter<?,?>> converters = new ArrayList<>();
        converters.add(new NoteReadConverter());
        converters.add(new NoteWriteConverter());
        return new MongoCustomConversions(converters);
    }

    @Override
    public MongoClient mongoClient() {
        return new MongoClient(this.hostName, this.port);
    }
}