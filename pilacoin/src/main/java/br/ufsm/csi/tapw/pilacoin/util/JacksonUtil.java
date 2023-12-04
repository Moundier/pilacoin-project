package br.ufsm.csi.tapw.pilacoin.util;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.SneakyThrows;

public class JacksonUtil {

    private static ObjectMapper getMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(Difficulty.class, new DifficultyDeserializer());

        mapper.registerModule(module);

        return mapper;
        // return new ObjectMapper().registerModule(new
        // SimpleModule().addDeserializer(Difficulty.class, new
        // DifficultyDeserializer()))
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T convert(String jsonString, Object valueType) {

        // Null check for jsonString
        if (jsonString == null) {
            throw new IllegalArgumentException("4.1 Input JSON string is null");
        }

        ObjectMapper mapper = getMapper();

        if (valueType instanceof Class) {
            try {
                return mapper.readValue(jsonString, (Class<T>) valueType);
            } catch (IOException e) {
                // Handle or log the exception, or rethrow if needed
                throw new IOException("Error during JSON deserialization", e);
            }
        } else if (valueType instanceof TypeReference<?>) {
            try {
                return mapper.readValue(jsonString, (TypeReference<T>) valueType);
            } catch (IOException e) {
                // Handle or log the exception, or rethrow if needed
                throw new IOException("Error during JSON deserialization", e);
            }
        } else {
            throw new IllegalArgumentException("Unsupported valueType");
        }
    }

    @SneakyThrows
    public static <T> T reader(String query, Class<T> valueType) {
        T result = new ObjectMapper().readValue(query, valueType);

        if (result == null) {
            throw new RuntimeException("Deserialization resulted in null value");
        }

        return result;
    }

    @SneakyThrows
    public static <T> String toString(T object) {
        return getMapper().writeValueAsString(object);
    }
}