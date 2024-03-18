package com.ss.song.interf;

/**
 * author shangsong 2024/1/9
 */
@FunctionalInterface
public interface FlankerFilter<T>
{
    boolean filter(T data);
}

