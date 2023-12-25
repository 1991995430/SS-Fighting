package com.ss.song.exception;

import com.ss.song.rest.RestResponse;

public interface StandardThrowable
{
	String getErrorMessage();

	String getErrorCode();

	void writeTo(RestResponse response);
}
