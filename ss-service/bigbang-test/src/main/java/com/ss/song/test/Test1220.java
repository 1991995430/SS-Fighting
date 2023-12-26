package com.ss.song.test;

import com.ss.song.pojo.User;
import org.apache.commons.lang.reflect.FieldUtils;

import java.lang.reflect.Field;

/**
 * author shangsong 2023/12/20
 */
public class Test1220 {
    public static void main(String[] args) throws NoSuchFieldException {
        User user = new User();
        user.setName("shangsong");
        Class<? extends User> aClass = user.getClass();
        // Field field = aClass.getDeclaredField("name");
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field field : declaredFields) {
            Object fieldValue = getFieldValue(user, field);
            System.out.println(fieldValue);
        }
    }

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
            System.out.println("getFieldValue failed! object:" + object + ",field:" + field.getName() + e.getMessage());
            return null;
        }
    }
}
