package com.ss.song.service;

import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * author shangsong 2024/1/9
 */
@Service
public class TestFilterService {

    public static void queryS() {
        Stream<String> zz = getAA(String.class, data -> data.equals("zz"));
        List<String> stringList = zz.collect(Collectors.toList());
        System.out.println(stringList);

        Stream<String> mm = getAA(String.class, data -> data.startsWith("s"));
        List<String> ss = mm.collect(Collectors.toList());
        System.out.println(ss);
    }

    private static <T> Stream<T> getAA(Class<T> tClass, FlankerFilter<String> filter) {
        List<String> list = new ArrayList<>();
        list.add("ss1");
        list.add("zz");
        list.add("ls");
        list.add("ww");
        return list.stream().filter(filter::filter).map(tClass::cast);

    }

    private static <T> Stream<T> getBB(Class<T> tClass, FlankerFilter<String> filter) {
        List<String> list = new ArrayList<>();
        list.add("ss1");
        list.add("zz");
        list.add("ls");
        list.add("ww");
        return list.stream().filter(filter::filter).map(tClass::cast);

    }

    public static void main(String[] args) {
        queryS();
    }
}
