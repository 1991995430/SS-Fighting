package com.ss.song.utils;

import com.ss.song.annotation.*;
import com.ss.song.xml.Parentable;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

public final class XmlParseUtils
{
	public static final String PASSWORD = "org.polaris.framework";

	public static <T> T parseObject(Class<T> cl, InputStream is) throws Exception
	{
		SAXReader reader = new SAXReader();
		Document document = reader.read(is);
		Element root = document.getRootElement();
		return parseObject(cl, root);
	}

	public static <T> T parseObject(Class<T> cl, File xmlFile) throws Exception
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(xmlFile);
			return parseObject(cl, is);
		}
		catch (Exception e)
		{
			throw new Exception("parseObject failed! xmlFile: " + xmlFile.getAbsolutePath(), e);
		}
		finally
		{
			IOUtils.closeQuietly(is);
		}
	}

	/**
	 * 根据类型和根节点Element生成对象
	 *
	 * @param cl
	 * @param element
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseObject(Class<T> cl, Element element) throws Exception
	{
		if (cl == null)
		{
			throw new IllegalArgumentException("target object is null!");
		}
		T target = cl.newInstance();
		Field[] fields = ReflectUtils.getAllFields(cl);
		for (Field field : fields)
		{
			if (field.getType().isAssignableFrom(Element.class))
			{
				// 对于Element的属性,直接予以设置
				field.setAccessible(true);
				field.set(target, element);
			}
			else if (field.isAnnotationPresent(XmlAttribute.class))
			{
				XmlAttribute xmlAttribute = field.getAnnotation(XmlAttribute.class);
				String fieldName = xmlAttribute.name();
				if (fieldName == null || fieldName.length() < 1)
				{
					fieldName = field.getName();
				}
				String value = element.attributeValue(fieldName);
				if (value == null)
				{
					continue;
				}
				field.setAccessible(true);
				if (field.getType() == String.class)
				{
					if (xmlAttribute.encrypt())
					{
						// 被加密
						value = AESUtils.decrypt(value, PASSWORD);// 还原成明文
					}
					field.set(target, value);
				}
				else
				{
					Object attribute = MyConvertUtils.convert(value, field.getType());
					field.set(target, attribute);
				}
			}
			else if (field.isAnnotationPresent(XmlAttributeMap.class))
			{
				field.setAccessible(true);
				Map<String, String> attributeMap = (Map<String, String>) field.get(target);
				if (attributeMap == null)
				{
					attributeMap = new HashMap<String, String>();
					field.set(target, attributeMap);
				}
				Iterator<?> iterator = element.attributeIterator();
				while (iterator.hasNext())
				{
					Attribute attribute = (Attribute) iterator.next();
					attributeMap.put(attribute.getName(), attribute.getValue());
				}
			}
			else if (field.isAnnotationPresent(XmlContent.class))
			{
				XmlContent xmlContent = field.getAnnotation(XmlContent.class);
				String name = xmlContent.name();
				if (name == null || StringUtils.isEmpty(name))
				{
					name = field.getName();
				}
				Element childElement = element.element(name);
				if (childElement == null)
				{
					childElement = element;
				}
				String value = childElement.getText();
				if (value == null)
				{
					value = "";
				}
				else
				{
					value = value.trim();
				}
				field.setAccessible(true);
				if (field.getType() == String.class)
				{
					field.set(target, value);
				}
				else
				{
					Object attribute = MyConvertUtils.convert(value, field.getType());
					field.set(target, attribute);
				}
			}
			else if (field.isAnnotationPresent(XmlCurrentTime.class))
			{
				field.setAccessible(true);
				if (field.getType() == Long.class)
				{
					field.set(target, System.currentTimeMillis());
				}
				else if (field.getType() == Date.class)
				{
					field.set(target, new Date(System.currentTimeMillis()));
				}
				else
				{
					throw new RuntimeException("UnSupport type: " + field.getType() + " of CurrentTime field: " + field.getName());
				}
			}
			else if (field.isAnnotationPresent(XmlObject.class))
			{
				XmlObject xmlObject = field.getAnnotation(XmlObject.class);
				String fieldName = xmlObject.name();
				if (fieldName == null || fieldName.length() < 1)
				{
					fieldName = field.getName();
				}
				Element childElement = element.element(fieldName);
				if (childElement == null)
				{
					continue;
				}
				Class<?> fieldClass = field.getType();
				Object childTarget = parseObject(fieldClass, childElement);
				field.setAccessible(true);
				field.set(target, childTarget);
				checkAndSetParent(target, childTarget);
			}
			else if (field.isAnnotationPresent(XmlList.class))
			{
				XmlList xmlChildren = field.getAnnotation(XmlList.class);
				String fieldName = xmlChildren.name();
				if (fieldName == null || fieldName.length() < 1)
				{
					fieldName = field.getName();
				}
				Iterator<Element> it = (Iterator<Element>) element.elementIterator();
				while (it.hasNext())
				{
					Element childElement = it.next();
					if (!childElement.getName().equalsIgnoreCase(fieldName))
					{
						continue;
					}
					Object childTarget = parseObject(xmlChildren.itemClass(), childElement);
					field.setAccessible(true);
					Collection<Object> list = (Collection<Object>) field.get(target);
					if (list == null)
					{
						list = (Collection<Object>) xmlChildren.listClass().newInstance();
						field.set(target, list);
					}
					list.add(childTarget);
					checkAndSetParent(target, childTarget);
				}
			}
			else if (field.isAnnotationPresent(XmlMixedList.class))
			{
				// 混合列表
				XmlMixedList xmlMixedList = field.getAnnotation(XmlMixedList.class);
				Iterator<Element> it = (Iterator<Element>) element.elementIterator();
				while (it.hasNext())
				{
					Element childElement = it.next();
					String name = childElement.getName();
					XmlEntry xmlEntry = findXmlEntryByName(xmlMixedList, name);
					if (xmlEntry == null)
					{
						continue;
					}
					Object childTarget = parseObject(xmlEntry.type(), childElement);
					field.setAccessible(true);
					Collection<Object> list = (Collection<Object>) field.get(target);
					if (list == null)
					{
						list = new ArrayList<Object>();
						field.set(target, list);
					}
					list.add(childTarget);
					checkAndSetParent(target, childTarget);
				}
			}
			else if (field.isAnnotationPresent(XmlMap.class))
			{
				XmlMap xmlChildren = field.getAnnotation(XmlMap.class);
				String fieldName = xmlChildren.name();
				if (fieldName == null || fieldName.length() < 1)
				{
					fieldName = field.getName();
				}
				Iterator<Element> it = (Iterator<Element>) element.elementIterator();
				while (it.hasNext())
				{
					Element childElement = it.next();
					if (!childElement.getName().equalsIgnoreCase(fieldName))
					{
						continue;
					}
					Object childTarget = parseObject(xmlChildren.valueClass(), childElement);
					String keyValue = childElement.attributeValue(xmlChildren.key());
					Object key = keyValue;
					if (xmlChildren.keyClass() != String.class)
					{
						key = MyConvertUtils.convert(keyValue, xmlChildren.keyClass());
					}
					field.setAccessible(true);
					Map<Object, Object> map = (Map<Object, Object>) field.get(target);
					if (map == null)
					{
						map = (Map<Object, Object>) xmlChildren.mapClass().newInstance();
						field.set(target, map);
					}
					if (!xmlChildren.reduplicatable())
					{
						if (map.containsKey(key))
						{
							throw new RuntimeException("Reduplicate key define! key=" + key + ",value=" + childTarget);
						}
					}
					map.put(key, childTarget);
					checkAndSetParent(target, childTarget);
				}
			}
		}
		return target;
	}

	/**
	 * 检查并且设置孩子节点的父对象
	 *
	 * @param parent
	 * @param child
	 */
	private static void checkAndSetParent(Object parent, Object child)
	{
		if (child instanceof Parentable)
		{
			Parentable parentable = (Parentable) child;
			parentable.setParent(parent);
		}
	}

	private static XmlEntry findXmlEntryByName(XmlMixedList xmlMixedList, String name)
	{
		for (XmlEntry xmlEntry : xmlMixedList.entrys())
		{
			if (StringUtils.equalsIgnoreCase(xmlEntry.name(), name))
			{
				return xmlEntry;
			}
		}
		return null;
	}

	private XmlParseUtils()
	{
	}
}
