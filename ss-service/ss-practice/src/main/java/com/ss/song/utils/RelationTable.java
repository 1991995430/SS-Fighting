package com.ss.song.utils;

public final class RelationTable
{
	private Class<?> left;
	private Class<?> right;
	private String name;
	private String leftColumnName;
	private String rightColumnName;

	public RelationTable(Class<?> left, Class<?> right)
	{
		if (left == null || right == null || left == right)
		{
			throw new IllegalArgumentException("left and right cannot null or equals!");
		}
		this.left = left;
		this.right = right;
		this.name = "rel_" + this.left.getSimpleName() + "_" + this.right.getSimpleName();
		this.leftColumnName = this.left.getSimpleName() + "_id";
		this.rightColumnName = this.right.getSimpleName() + "_id";
	}

	public String toColumnName(Class<?> tableClass)
	{
		if (tableClass == this.left)
		{
			return this.leftColumnName;
		}
		else if (tableClass == this.right)
		{
			return this.rightColumnName;
		}
		else
		{
			throw new IllegalArgumentException("invalid tableClass: " + tableClass + " of RelationTable: " + this);
		}
	}

	public String toOtherColumnName(Class<?> tableClass)
	{
		if (tableClass == this.left)
		{
			return this.rightColumnName;
		}
		else if (tableClass == this.right)
		{
			return this.leftColumnName;
		}
		else
		{
			throw new IllegalArgumentException("invalid tableClass: " + tableClass + " of RelationTable: " + this);
		}
	}

	public Class<?> toOtherTableClass(Class<?> tableClass)
	{
		if (tableClass == this.left)
		{
			return this.right;
		}
		else if (tableClass == this.right)
		{
			return this.left;
		}
		else
		{
			throw new IllegalArgumentException("invalid tableClass: " + tableClass + " of RelationTable: " + this);
		}
	}

	public String[] getColumnNames()
	{
		return new String[] { this.leftColumnName, this.rightColumnName };
	}

	public String getName()
	{
		return name;
	}

	public String getLeftColumnName()
	{
		return leftColumnName;
	}

	public String getRightColumnName()
	{
		return rightColumnName;
	}

	public Class<?> getLeft()
	{
		return left;
	}

	public Class<?> getRight()
	{
		return right;
	}

	@Override
	public String toString()
	{
		return "Rel: " + this.left.getSimpleName() + " -> " + this.right.getSimpleName();
	}

	@Override
	public int hashCode()
	{
		return this.left.hashCode() * 3 + this.right.hashCode() * 7;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == null || !(other instanceof RelationTable))
		{
			return false;
		}
		RelationTable relationTable = (RelationTable) other;
		return this.left == relationTable.left && this.right == relationTable.right;
	}

}
