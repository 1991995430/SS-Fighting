package com.ss.song.test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * author shangsong 2023/11/30
 */
public class Test1130 {

    public static void main(String[] args) throws ClassNotFoundException {
        /*Class<?> dataDto = Class.forName("com.ss.song.mapper.dao.DataDto");
        Annotation annotation = dataDto.getAnnotation(Annotation.class);*/
        List<String> commandList = new ArrayList<>();
        commandList.add("ll");
        commandList.add("ls");
        commandList.add("mkdir sd");
        String[] strings = commandList.toArray(new String[0]);
        Object[] objects = commandList.toArray();
        for (Object o : objects) {
            if (o instanceof String) {
                System.out.println(o);
            }
        }
        System.out.println(strings);
        aa("222");
    }

    public static void aa(String... ab) {

    }

}
