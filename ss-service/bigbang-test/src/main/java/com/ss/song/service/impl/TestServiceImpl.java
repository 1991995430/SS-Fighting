package com.ss.song.service.impl;

import com.ss.song.enums.FetchMode;
import com.ss.song.service.FlankerFilter;
import com.ss.song.service.TestService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * author shangsong 2023/12/26
 */
public class TestServiceImpl implements TestService {

    public static void main(String[] args) {
        List<String> list = queryForList(String.class);
        System.out.println(list);
    }


    static <T> List<T> queryForList(Class<T> tClass)
    {
        return queryForStream1(tClass).collect(Collectors.toList());
    }

    public static <T> Stream<T> queryForStream1(Class<T> tClass)
    {
        List<String> arr = new ArrayList<>();
        arr.add("11");
        arr.add("22");
        return arr.stream().map(tClass::cast);
    }

    @Override
    public <T> Stream<T> queryForStream(Class<T> tClass) {
        return null;
    }
}
