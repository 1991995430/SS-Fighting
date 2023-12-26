package com.ss.song.annotation;

import com.ss.song.enums.RelationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义与外部根表的多对多关系
 *
 * @author wang.sheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlankerRelation
{
	/**
	 * 多对多的对端表(必须是根表)
	 *
	 * @return
	 */
	Class<?> target();

	/**
	 * 注解所在类是左表还是右表
	 *
	 * @return
	 */
	RelationType relationType() default RelationType.LEFT;
}
