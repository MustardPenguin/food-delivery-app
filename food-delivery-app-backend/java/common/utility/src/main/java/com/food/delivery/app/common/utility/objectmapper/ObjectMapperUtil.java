package com.food.delivery.app.common.utility.objectmapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObjectMapperUtil {

    private final ObjectMapper objectMapper;

    public ObjectMapperUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> String convertObjectToJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Error converting object of {} to string", object.getClass());
            throw new RuntimeException("Error converting object of " + object.getClass() + " to class. Error: " + e.getMessage());
        }
    }

    public <T> T convertJsonToObject(String json, Class<T> classType) {
        try {
            return objectMapper.readValue(json, classType);
        } catch (Exception e) {
            log.error("Error converting json of {} to a {}", json, classType.getName());
            throw new RuntimeException("Error converting json of " + json + " to " + classType.getName() + ". Error: " + e.getMessage());
        }
    }
}
