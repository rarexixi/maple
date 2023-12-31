package org.xi.maple.common.util;

import java.util.HashMap;
import java.util.Map;

public class MapUtils {

    /**
     * 合并两个map，如果其中一个为null，则返回另一个，如果两个都不为null，则返回合并后的新map
     *
     * @param map1
     * @param map2
     * @param <K>  key
     * @param <V>  value
     * @return
     */
    public static <K, V> Map<K, V> mergeMap(Map<K, V> map1, Map<K, V> map2) {
        if (map1 == null) {
            return map2;
        }
        if (map2 == null) {
            return map1;
        }
        Map<K, V> newMap = new HashMap<>(map1.size() + map2.size());
        newMap.putAll(map1);
        newMap.putAll(map2);
        return newMap;
    }

    /**
     * 合并两个map，返回合并后的新map
     *
     * @param map1
     * @param map2
     * @param <K>  key
     * @param <V>  value
     * @return
     */
    public static <K, V> Map<K, V> mergeToNewMap(Map<K, V> map1, Map<K, V> map2) {
        Map<K, V> newMap = new HashMap<>();
        if (map1 != null && !map1.isEmpty()) {
            newMap.putAll(map1);
        }
        if (map2 != null && !map2.isEmpty()) {
            newMap.putAll(map2);
        }
        return newMap;
    }
}
