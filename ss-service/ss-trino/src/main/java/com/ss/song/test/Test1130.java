package com.ss.song.test;

import java.lang.annotation.Annotation;

/**
 * author shangsong 2023/11/30
 */
public class Test1130 {

    public static void main(String[] args) throws ClassNotFoundException {
        Class<?> dataDto = Class.forName("com.ss.song.mapper.dao.DataDto");
        Annotation annotation = dataDto.getAnnotation(Annotation.class);


    }

}
