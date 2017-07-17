package renue.fts.gateway.admin.autotest.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

/**
 * Utility for reflection API.
 */
public final class ReflectionUtility {
    private ReflectionUtility(){}

    /**
     * Get all field in class and super classes, exclude Object.
     * @param clazz Class which fields get.
     * @param fields List of fields. Generally necessary for method rec
     * @return
     */
    public static Field[] getAllFields(final Class clazz, final List<Field> fields) {
        if (clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            getAllFields(clazz.getSuperclass(), fields);
        }
        return fields.toArray(new Field[fields.size()]);
    }
}
