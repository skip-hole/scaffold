package com.scaffold.webflux.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public abstract class JacksonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> String toJson(T obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(String src, Class<T> clazz) {
        try {
            return objectMapper.readValue(src, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> byte[] serialize(T obj) {
        if (obj == null) return null;
        try {
            return objectMapper.writeValueAsString(obj).getBytes(StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(byte[] src, Class<T> clazz) {
        try {
            return objectMapper.readValue(new String(src, StandardCharsets.UTF_8), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
