package com.jian.community.infrastructure.util;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class InMemoryReflectionHelper {

    public static Long getId(Object entity) {
        Field field = findField(entity.getClass(), "id");
        if (field == null) return null;

        field.setAccessible(true);
        try {
            return (Long) field.get(entity);

        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static void setId(Object entity, Long id) {
        setField(entity, "id", id);
    }

    public static void setCreatedAt(Object entity, LocalDateTime createdAt) {
        setField(entity, "createdAt", createdAt);
    }

    public static void setUpdatedAt(Object entity, LocalDateTime updatedAt) {
        setField(entity, "updatedAt", updatedAt);
    }

    private static void setField(Object entity, String name, Object value) {
        Field field = findField(entity.getClass(), name);
        if (field == null) return;

        field.setAccessible(true);
        try {
            field.set(entity, value);
        } catch (IllegalAccessException ignored) {}
    }

    private static Field findField(Class<?> clazz, String name) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(name);

            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}
