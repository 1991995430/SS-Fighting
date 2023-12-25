package com.ss.song.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FlankerTable
{
	/**
	 * 实际存储名
	 *
	 * @return
	 */
	String name() default "";

	/**
	 * 是否需要缓存
	 *
	 * @return
	 */
	boolean cached() default true;
}
