package com.ss.song.enums;

public interface Displayable
{
	String display();

	String name();

	default boolean ignoreNONE()
	{
		return true;
	}
}
