package com.ss.song.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标识为上一级对象实例<br>
 * 该实例不支持JSON序列化,避免无穷递归<br>
 *
 * @author wang.sheng
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FlankerParent
{
}
