package com.ss.song.annotation;

import com.ss.song.enums.GeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlankerKey
{
	/**
	 * 字段名.默认取类属性名
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * 取值长度限制
	 *
	 * @return
	 */
	int length() default 32;

	/**
	 * 主键生成器,默认UUID
	 *
	 * @return
	 */
	GeneratorType generatorType() default GeneratorType.UUID;
}
