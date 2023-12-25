package com.ss.song.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.commons.lang.reflect.MethodUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 类反射所使用的工具
 *
 * @author wang.sheng
 */
public final class ReflectUtils
{
	static Log log = LogFactory.getLog(ReflectUtils.class);

	private ReflectUtils()
	{
	}

	/**
	 * 判断两个对象中指定注解的字段是否完全相等
	 *
	 * @param arg1
	 * @param arg2
	 * @param annotationClass
	 * @return
	 */
	public static boolean equals(Object arg1, Object arg2, Class<? extends Annotation> annotationClass)
	{
		if ((arg1 == null && arg2 == null) || arg1 == arg2)
		{
			return true;
		}
		if (arg1 == null || arg2 == null || arg1.getClass() != arg2.getClass())
		{
			return false;
		}
		Field[] fields = getAllFields(arg1.getClass(), annotationClass);
		for (Field field : fields)
		{
			Object value1 = getFieldValue(arg1, field);
			Object value2 = getFieldValue(arg2, field);
			if ((value1 == null && value2 == null) || value1 == value2)
			{
				continue;
			}
			if (value1 == null || value2 == null)
			{
				return false;
			}
			if (field.getType() == BigDecimal.class)
			{
				BigDecimal decimal1 = (BigDecimal) value1;
				BigDecimal decimal2 = (BigDecimal) value2;
				if (decimal1.compareTo(decimal2) != 0)
				{
					return false;
				}
			}
			else if (!value1.equals(value2))
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 获取类下面的所有属性,包括父类
	 *
	 * @param cl
	 * @return
	 */
	public static Field[] getAllFields(Class<?> cl)
	{
		return getFields(cl).toArray(new Field[0]);
	}

	public static Field[] getAllFields(Class<?> clazz, Class<? extends Annotation> annotationClass)
	{
		return getFields(clazz, annotationClass).toArray(new Field[0]);
	}

	/**
	 * 获取类下面所有的方法,包括父类
	 *
	 * @param cl
	 * @return
	 */
	public static Method[] getAllMethods(Class<?> cl)
	{
		return getMethods(cl).toArray(new Method[0]);
	}

	public static Method[] getAllMethods(Class<?> clazz, Class<? extends Annotation> annotationClass)
	{
		return getMethods(clazz, annotationClass).toArray(new Method[0]);
	}

	public static Method[] getMethodsByName(Class<?> clazz, String name)
	{
		List<Method> list = new ArrayList<>();
		for (Method method : getMethods(clazz))
		{
			if (StringUtils.equals(method.getName(), name))
			{
				list.add(method);
			}
		}
		return list.toArray(new Method[0]);
	}

	/**
	 * 深度查询一个对象内部的属性
	 *
	 * @param fieldNames
	 * @return
	 */
	public static Object getFieldValueByPath(Object object, String[] fieldNames)
	{
		Object value = null;
		for (String fieldName : fieldNames)
		{
			value = getFieldValue(object, fieldName);
			if (value == null)
			{
				return null;
			}
			object = value;
		}
		return value;
	}

	/**
	 * 通过对象的getter方法获取数据
	 *
	 * @param object
	 * @param name
	 * @return
	 */
	public static Object getProperty(Object object, String name)
	{
		String methodName = "get" + Character.toUpperCase(name.charAt(0)) + StringUtils.substring(name, 1);
		try
		{
			Method method = MethodUtils.getAccessibleMethod(object.getClass(), methodName, new Class[0]);
			if (method == null)
			{
				throw new RuntimeException("method not exists of " + methodName + " class: " + object.getClass().getName());
			}
			return method.invoke(object, new Object[0]);
		}
		catch (Exception e)
		{
			log.warn("getProperty failed! object: " + object + ", property name: " + name);
			return null;
		}
	}

	/**
	 * 根据Field获取属性取值
	 *
	 * @param object
	 * @param field
	 * @return
	 */
	public static Object getFieldValue(Object object, Field field)
	{
		if (object == null || field == null)
		{
			return null;
		}
		try
		{
			return FieldUtils.readField(field, object, true);
		}
		catch (IllegalAccessException e)
		{
			log.warn("getFieldValue failed! object:" + object + ",field:" + field.getName(), e);
			return null;
		}
	}

	/**
	 * 根据Field和Value设置对象属性
	 *
	 * @param object
	 * @param field
	 * @param value
	 */
	public static void setFieldValue(Object object, Field field, Object value)
	{
		if (object == null || field == null)
		{
			return;
		}
		try
		{
			FieldUtils.writeField(field, object, value, true);
		}
		catch (IllegalAccessException e)
		{
			log.warn("setFieldValue failed! object:" + object + ",field:" + field.getName(), e);
		}
	}

	/**
	 * 根据属性名获取属性取值
	 *
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName)
	{
		if (object == null || fieldName == null)
		{
			return null;
		}
		try
		{
			return FieldUtils.readField(object, fieldName, true);
		}
		catch (IllegalAccessException e)
		{
			log.warn("getFieldValue failed! object:" + object + ",fieldName:" + fieldName, e);
			return null;
		}
	}

	/**
	 * 根据Method实例获取属性名
	 *
	 * @param method
	 * @return
	 */
	public static String getFieldNameByMethod(Method method)
	{
		if (method == null)
		{
			return null;
		}
		String fieldName = method.getName();
		if (fieldName.startsWith("set") || fieldName.startsWith("get"))
		{
			char[] chs = fieldName.substring(3).toCharArray();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < chs.length; i++)
			{
				if (i == 0)
				{
					buffer.append(Character.toLowerCase(chs[i]));
				}
				else
				{
					buffer.append(chs[i]);
				}
			}
			fieldName = buffer.toString();
		}
		return fieldName;
	}

	private static List<Method> getMethods(Class<?> cl)
	{
		if (cl == Object.class)
		{
			return new ArrayList<Method>();
		}
		else
		{
			List<Method> list = new ArrayList<Method>();
			Method[] methods = cl.getDeclaredMethods();
			for (Method method : methods)
			{
				list.add(method);
			}
			list.addAll(getMethods(cl.getSuperclass()));// 递归调用
			return list;
		}
	}

	private static List<Method> getMethods(Class<?> cl, Class<? extends Annotation> annotationClass)
	{
		if (cl == Object.class)
		{
			return new ArrayList<Method>();
		}
		else
		{
			List<Method> list = new ArrayList<Method>();
			Method[] methods = cl.getDeclaredMethods();
			for (Method method : methods)
			{
				if (!method.isAnnotationPresent(annotationClass))
				{
					continue;
				}
				list.add(method);
			}
			list.addAll(getMethods(cl.getSuperclass(), annotationClass));// 递归调用
			return list;
		}
	}

	private static List<Field> getFields(Class<?> cl, Class<? extends Annotation> annotationClass)
	{
		if (cl == Object.class)
		{
			return new ArrayList<Field>();
		}
		else
		{
			List<Field> list = new ArrayList<Field>();
			Field[] fields = cl.getDeclaredFields();
			for (Field field : fields)
			{
				if (!field.isAnnotationPresent(annotationClass))
				{
					continue;
				}
				list.add(field);
			}
			list.addAll(getFields(cl.getSuperclass(), annotationClass));// 递归调用
			return list;
		}
	}

	private static List<Field> getFields(Class<?> cl)
	{
		if (cl == Object.class)
		{
			return new ArrayList<Field>();
		}
		else
		{
			List<Field> list = new ArrayList<Field>();
			Field[] fields = cl.getDeclaredFields();
			for (Field field : fields)
			{
				list.add(field);
			}
			list.addAll(getFields(cl.getSuperclass()));// 递归调用
			return list;
		}
	}

}
