package com.ss.song.test;

import com.ss.song.interf.Converter;

/**
 * author shangsong 2023/12/21
 */
public class ConverterDemo {
    public static void main(String[] args) {
        useConverter(Integer::parseInt);
    }

    private static void useConverter(Converter converter) {
        int num = converter.converter("234") + 20;
        System.out.println(num);
    }
}
