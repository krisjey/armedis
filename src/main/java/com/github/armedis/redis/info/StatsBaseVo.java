/**
 * 
 */
package com.github.armedis.redis.info;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.base.CaseFormat;

/**
 * 
 */
public class StatsBaseVo {
	protected String sctionContent;

	public StatsBaseVo() {

	}

	@Override
	public final String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	/**
	 * TODO Generic으로 처리.
	 * 
	 * @param <T>
	 * 
	 * @param content
	 * @return T
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public static <T> T fromString(Class<T> clazz, String content)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		T instance = clazz.getDeclaredConstructor().newInstance();

		if (StringUtils.trimToNull(content) == null) {
			return instance;
		}

		String[] lines = content.split("\r\n");

		for (String line : lines) {
			String[] parts = line.split(":");
			if (parts.length == 2) {
				String key = parts[0].trim();
				String value = parts[1].trim();

				key = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key);

				ReflectionSetter.setFieldValue(instance, key, value);
			}
		}

		return instance;
	}

	/**
	 * @return the sctionContent
	 */
	public String getSctionContent() {
		return sctionContent;
	}

	/**
	 * @param sctionContent the sctionContent to set
	 */
	@SuppressWarnings("unchecked")
	protected <T> T setSctionContent(String sctionContent) {
		this.sctionContent = sctionContent;
		return (T) this;
	}
}
