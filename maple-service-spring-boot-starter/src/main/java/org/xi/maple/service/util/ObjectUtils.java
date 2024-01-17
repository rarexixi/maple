package org.xi.maple.service.util;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class ObjectUtils {
    public static <S, T> T copy(S source, Class<T> clazz, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        try {
            T target = clazz.newInstance();
            BeanUtils.copyProperties(source, target, ignoreProperties);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <S, T> List<T> copy(Collection<S> source, Class<T> clazz, String... ignoreProperties) {
        return copy(source, clazz, null, null, ignoreProperties);
    }

    public static <S, T> List<T> copy(Collection<S> source, Class<T> clazz, Consumer<T> consumer, String... ignoreProperties) {
        return copy(source, clazz, null, consumer, ignoreProperties);
    }

    public static <S, T> List<T> copy(Collection<S> source, Class<T> clazz, Function<S, Boolean> filter, String... ignoreProperties) {
        return copy(source, clazz, filter, null, ignoreProperties);
    }

    public static <S, T> List<T> copy(Collection<S> source, Class<T> clazz, Function<S, Boolean> filter, Consumer<T> consumer, String... ignoreProperties) {
        if (source == null) {
            return null;
        }
        final List<T> list = new ArrayList<>(source.size());
        for (S item : source) {
            T target;
            if ((filter == null || filter.apply(item)) && (target = copy(item, clazz, ignoreProperties)) != null) {
                if (consumer != null) {
                    consumer.accept(target);
                }
                list.add(target);
            }
        }
        return list;
    }

    public static <T> T get(Map<String, ?> map, Class<T> clazz, String... ignoreProperties) {
        if (map == null) {
            return null;
        }
        try {
            T target = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            Set<String> ignorePropertySet = new HashSet<>(Arrays.asList(ignoreProperties));
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                if (ignorePropertySet.contains(field.getName()) || !map.containsKey(field.getName())) {
                    continue;
                }
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                field.set(target, map.get(field.getName()));
                field.setAccessible(accessible);
            }
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
