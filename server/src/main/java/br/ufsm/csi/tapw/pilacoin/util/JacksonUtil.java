package br.ufsm.csi.tapw.pilacoin.util;

import br.ufsm.csi.tapw.pilacoin.model.Difficulty;

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
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static <T> T convert(String jsonString, Object valueType) {

        if (jsonString == null) {
            throw new IllegalArgumentException("4.1 Input JSON string is null");
        }

        ObjectMapper mapper = getMapper();

        if (valueType instanceof Class)
            return mapper.readValue(jsonString, (Class<T>) valueType);
        else if (valueType instanceof TypeReference<?>) 
            return mapper.readValue(jsonString, (TypeReference<T>) valueType);
        else {
            throw new IllegalArgumentException("Unsupported valueType");
        }
    }

    @SneakyThrows
    public static <T> String toString(T object) {
        return getMapper().writeValueAsString(object);
    }
}