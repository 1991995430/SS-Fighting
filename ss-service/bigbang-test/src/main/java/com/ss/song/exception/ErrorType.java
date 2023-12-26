package com.ss.song.exception;

/**
 * 平台错误信息全局封装
 *
 * @author wang.sheng
 *
 */
public interface ErrorType
{
	/**
	 * 错误码
	 *
	 * @return
	 */
	String code();

	/**
	 * 错误消息
	 *
	 * @return
	 */
	String message();

	/**
	 * 格式化错误消息
	 *
	 * @param args
	 * @return
	 */
	default String formatError(Object... args)
	{
		return String.format(this.message(), args) + "(" + this.code() + ")";
	}

	enum Validation implements ErrorType
	{
		FORM("表单数据校验错误");

		private String message;

		private Validation(String message)
		{
			this.message = message;
		}

		@Override
		public String toString()
		{
			return super.name() + "(" + this.message + ")";
		}

		@Override
		public String code()
		{
			return this.name();
		}

		@Override
		public String message()
		{
			return this.message;
		}

	}
}
