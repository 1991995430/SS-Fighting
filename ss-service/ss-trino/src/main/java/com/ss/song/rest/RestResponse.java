package com.ss.song.rest;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 标准化的Rest接口响应
 *
 * @author wang.sheng
 *
 */
public class RestResponse
{
	private Object data;
	private boolean success = true;
	private String message;
	/**
	 * 错误码
	 */
	private String errorCode;
	private final Map<String, String> errors = new LinkedHashMap<String, String>();

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public void addError(String id, String message)
	{
		errors.put(id, message);
	}

	public Map<String, String> getErrors()
	{
		return errors;
	}

	public boolean hasErrors()
	{
		return !errors.isEmpty();
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

}
