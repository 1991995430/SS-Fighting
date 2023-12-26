package com.ss.song.test;

import com.ss.song.annotation.Permission;

import java.lang.reflect.Method;

/**
 * author shangsong 2023/12/19
 */
public class PermissionChecker {
    public static boolean hasPermission(Object object, String permission) {
        if (object == null || permission == null) {
            return false;
        }

        try {
            Class<?> clazz = object.getClass();
            Method[] methods = clazz.getDeclaredMethods();

            for (Method method : methods) {
                Permission annotation = method.getAnnotation(Permission.class);
                if (annotation != null && annotation.value().equals(permission)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
