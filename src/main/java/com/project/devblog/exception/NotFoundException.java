package com.project.devblog.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class NotFoundException extends RuntimeException {

    public NotFoundException(Class<?> clazz, String id) {
        super(String.format("Entity %s with id %s not found", clazz.getSimpleName(), id));
    }

    public NotFoundException(Class<?> clazz, String... searchParams) {
        super(NotFoundException.generateMessage(clazz.getSimpleName(), toMap(searchParams)));
    }

    private static String generateMessage(String entity, Map<String, String> searchParams) {
        return StringUtils.capitalize(entity) + " was not found for parameters " + searchParams;
    }

    private static Map<String, String> toMap(String... entries) {
        if (entries.length % 2 == 1) {
            throw new IllegalArgumentException("Invalid entries");
        }
        return IntStream.range(0, entries.length / 2)
                .map(i -> i * 2)
                .collect(HashMap::new, (m, i) -> m.put(entries[i], entries[i + 1]), Map::putAll);
    }

}
