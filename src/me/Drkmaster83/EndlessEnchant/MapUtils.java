package me.Drkmaster83.EndlessEnchant;

import java.util.Map;
import java.util.Map.Entry;

public class MapUtils
{
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (value.equals(entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
	}

	public static <T, E> void removeKeyAndValue(Map<T, E> map, Object key) {
		map.values().remove(map.get(key));
		map.remove(key);
	}

	public static void removeValueAndKey(Map<Object, Object> map, Object value) {
		map.values().remove(value);
		map.remove(getKeyByValue(map, value));
	}
}