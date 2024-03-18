package com.ss.song.test;

import com.ss.song.annotation.FlankerChildren;
import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerTable;
import com.ss.song.model.ComacTest;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;

/**
 * author shangsong 2024/1/10
 */
public class DetetorAnnotationTest {
    public static void main(String[] args) {
        ComacTest comacTest = new ComacTest();
        Class<? extends ComacTest> aClass = comacTest.getClass();
        Annotation[] annotations = aClass.getAnnotations();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> aClass1 = annotation.annotationType();
            System.out.println(aClass1.getName());
        }
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            /*System.out.println(field.getName() + ":::");
            for (Annotation annotation : field.getAnnotations()){
                Class<? extends Annotation> aClass1 = annotation.annotationType();
                if (aClass1.isAnnotationPresent(FlankerChildren.class)) {
                    System.out.println("FlankerChildrenAAAAAAAAAA");
                    boolean annotation1 = aClass1.isAnnotation();
                    if (annotation1) {
                        System.out.println("aa");
                    }
                }
                System.out.println(aClass1.getName());
            }*/
            if (field.isAnnotationPresent(FlankerChildren.class)) {
                System.out.println(field.getName());
                System.out.println("FlankerChildren.class.....");
            }
        }
        FlankerTable annotation = aClass.getAnnotation(FlankerTable.class);
        String name = annotation.name();
        System.out.println(name);
    }
}
