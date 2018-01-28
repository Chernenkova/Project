package edu.nc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonClassParser {

    public static byte[] getBytes(Object o) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getObject(byte[] bytes, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
