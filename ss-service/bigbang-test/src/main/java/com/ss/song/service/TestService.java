package com.ss.song.service;

import java.util.stream.Stream;

/**
 * author shangsong 2023/12/26
 */
public interface TestService {
    <T> Stream<T> queryForStream(Class<T> tClass);

    default void aa() {

    }
}
