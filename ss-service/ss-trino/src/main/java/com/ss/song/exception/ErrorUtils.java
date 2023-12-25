package com.ss.song.exception;

import com.ss.song.rest.RestResponse;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 错误信息提取工具类
 *
 * @author wang.sheng
 *
 */
public final class ErrorUtils
{
	/**
	 * 获取异常中的信息并写入RestResponse对象中
	 *
	 * @param e
	 * @param response
	 */
	public static void write(Exception e, RestResponse response)
	{
		if (e == null || response == null)
		{
			throw new IllegalArgumentException("Exception or RestResponse cannot null!");
		}
		Throwable cause = e;
		while (cause != null)
		{
			if (cause instanceof StandardThrowable)
			{
				((StandardThrowable) cause).writeTo(response);
				return;
			}
			cause = cause.getCause();
		}
		// 普通异常
		response.setSuccess(false);
		response.setErrorCode("SYSTEM_ERROR");
		response.setMessage(ExceptionUtils.getRootCauseMessage(e));
	}

	public static <T extends Throwable> T getRootCause(Throwable e, Class<T> throwableClass)
	{
		Throwable cause = e;
		while (cause != null)
		{
			if (throwableClass.isAssignableFrom(cause.getClass()))
			{
				return throwableClass.cast(cause);
			}
			cause = cause.getCause();
		}
		return null;
	}

	private ErrorUtils()
	{
	}
}
