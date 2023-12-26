package com.ss.song.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlankerChildren
{
	/**
	 * 子类的类型
	 *
	 * @return
	 */
	Class<?> childClass() default Object.class;

	/**
	 * 是否唯一
	 *
	 * @return
	 */
	boolean singleton() default false;
}
