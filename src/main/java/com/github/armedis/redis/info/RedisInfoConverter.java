/**
 * 
 */
package com.github.armedis.redis.info;

import java.lang.reflect.Field;

/**
 * 
 */
public class RedisInfoConverter {
	/**
	 * Private method to set field values using reflection
	 * 
	 * @param objects
	 * @param fieldName
	 * @param value
	 */
	public static void setField(Object objects, String fieldName, String value) {

		try {
			Field field = objects.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);

			if (field.getType() == int.class) {
				field.setInt(objects, Integer.parseInt(value));
			} else if (field.getType() == long.class) {
				field.setLong(objects, Long.parseLong(value));
			} else if (field.getType() == double.class) {
				field.setDouble(objects, Double.parseDouble(value));
			} else {
				field.set(objects, value);
			}
		} catch (NoSuchFieldException | IllegalAccessException | NumberFormatException e) {
			System.out.println(
					"Can not found decleared field in " + objects.getClass().getSimpleName() + " " + fieldName);
		}
	}
}
