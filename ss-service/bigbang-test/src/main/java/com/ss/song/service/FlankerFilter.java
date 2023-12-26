package com.ss.song.service;

@FunctionalInterface
public interface FlankerFilter<T>
{
	boolean filter(T data);
}
