package com.ss.song.enums;


import com.ss.song.exception.ErrorType;

public interface FlankerErrorType extends ErrorType
{
	public enum Metadata implements FlankerErrorType
	{
		TABLE_NOT_EXISTS("不存在的表: %s!"), INVALID_TABLE_CLASS("非法的对象类型: %s!"), KEY_NOT_EXISTS("主键字段不存在! 表: %s"), UNKNOWN_ENUM_TYPE("未知的枚举类型! 属性: %s"), PARENT_TABLE_EXISTS("上一级表已存在! 表: %s"), TABLE_NOT_ROOT("非根表! 表: %s"), INVALID_RELATION(
				"非法的多对多关系! %s -> %s"), RELATION_NOT_EXISTS("%s -> %s 之间的关系不存在!"), INVALID_RELATION_FIELD_DEFINE("非法的关系属性定义: %s");

		private String message;

		private Metadata(String message)
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

	public enum Query implements FlankerErrorType
	{
		COLUMN_CANNOT_NULL("字段: %s不可为NULL!"), UNKNOWN_FIELD_TYPE("不识别的属性类型: %s"), FOREIGN_KEY_NOT_EXISTS("上级表不存在! 类型: %s"), ROOT_DATA_NOT_EXISTS("根对象不存在! 类型: %s, key: %s"), PATH_DATA_NOT_EXISTS("指定路径的对象不存在! 路径: %s"), KEY_CANNOT_EMPTY(
				"主键不可为空!"), CACHED_CANNOT_OVERWRITE("已缓存对象不可用于覆盖操作! 对象路径: %s"), EMPTY_PATH("路径为空!"), PARENT_DATA_NOT_EXISTS("上一级数据对象不存在! 路径: %s"), DATA_NOT_EXISTS("数据对象不存在! 类型: %s, 对象ID: %s"), DATA_NOT_UNIQUE("数据对象不唯一! 类型: %s");

		private String message;

		private Query(String message)
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
