package edu.nc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonClassParser {

    private static volatile JsonClassParser instance;
    private ObjectMapper mapper;

    private JsonClassParser() {
        mapper = new ObjectMapper();
    }

    public static JsonClassParser getInstance() {
        if (null == instance) {
            synchronized (JsonClassParser.class) {
                if (null == instance) {
                    instance = new JsonClassParser();
                }
            }
        }
        return instance;
    }

    public byte[] getBytes(Object o) {
        try {
            return mapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T getObject(byte[] bytes, Class<T> clazz) {
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
