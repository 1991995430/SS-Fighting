package com.ss.song.test;

import com.ss.song.annotation.*;
import com.ss.song.enums.FlankerErrorType;
import com.ss.song.enums.None;
import com.ss.song.enums.SqlType;
import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.KeyMeta;
import com.ss.song.meta.RelationMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.User;
import com.ss.song.utils.ClassScanner;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.ReflectUtils;
import com.ss.song.utils.RelationTable;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * author shangsong 2023/12/25
 */
public class Test1225 {
    private static final Map<Class<?>, TableMeta> tableMetaMap = new HashMap<>();
    private static final Set<RelationTable> relationTableSet = new HashSet<>();

    public static void main(String[] args) {
        ClassScanner.scan("com.ss.song").filter(clazz -> clazz.isAnnotationPresent(FlankerTable.class)).map(clazz -> loadTableMeta(clazz)).forEach(tableMeta -> tableMetaMap.put(tableMeta.getTableClass(), tableMeta));
        System.out.println(tableMetaMap);
        TableMeta[] tableMetas = tableMetaMap.values().toArray(new TableMeta[0]);
        for (TableMeta meta : tableMetas)
        {
            Class<?> tableClass = meta.getTableClass();
            // 检查1对多子对象
            Field[] fields = ReflectUtils.getAllFields(tableClass, FlankerChildren.class);
            for (Field field : fields)
            {
                FlankerChildren children = field.getAnnotation(FlankerChildren.class);
                Class<?> childTableClass = children.singleton() ? field.getType() : children.childClass();
                if (childTableClass == Object.class)
                {
                    // 子表类型未设置
                    //throw new StandardException(FlankerErrorType.Metadata.INVALID_TABLE_CLASS, childTableClass.getName());
                }
                TableMeta childTableMeta = tableMetaMap.get(childTableClass);
                if (childTableMeta == null)
                {
                    // 子表不存在
                    //throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS, childTableClass.getName());
                }
                if (childTableMeta.getParent() != null)
                {
                    // 已设置父对象
                    //throw new StandardException(FlankerErrorType.Metadata.PARENT_TABLE_EXISTS, childTableClass.getName());
                }
                childTableMeta.setSingleton(children.singleton());
                meta.addChild(field, childTableMeta);
            }
        }
        System.out.println();
        // 初始化关系
        for (TableMeta leftTableMeta : tableMetas)
        {
            // 检查多对多子对象
            Field[] relationFields = ReflectUtils.getAllFields(leftTableMeta.getTableClass(), FlankerRelation.class);
            for (Field relationField : relationFields)
            {
                FlankerRelation flankerRelation = relationField.getAnnotation(FlankerRelation.class);
                if (!Set.class.isAssignableFrom(relationField.getType()))
                {
                    // 关系属性必须是Set<String>集合类型
                   // throw new StandardException(FlankerErrorType.Metadata.INVALID_RELATION_FIELD_DEFINE, relationField.getType().getName());
                }
                Class<?> otherTableClass = flankerRelation.target();
                TableMeta rightTableMeta = getRootTableMeta(otherTableClass);// 找到关联表对应的元数据,对端必须是根表
                switch (flankerRelation.relationType())
                {
                    case LEFT:
                        if (!rightTableMeta.isRoot())
                        {
                            // 右表必须是根表
                            //throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_ROOT, otherTableClass.getName());
                        }
                        break;
                    case RIGHT:
                        if (!leftTableMeta.isRoot())
                        {
                            // 右表必须是根表
                           // throw new StandardException(FlankerErrorType.Metadata.TABLE_NOT_ROOT, leftTableMeta.getTableClass().getName());
                        }
                        break;
                }
                RelationMeta relationMeta = new RelationMeta(relationField, flankerRelation.relationType(), leftTableMeta.getTableClass(), rightTableMeta.getTableClass());
                leftTableMeta.addRelationMeta(relationMeta);// 添加多对多关系映射
                relationTableSet.add(relationMeta.getRelationTable());
            }
        }

    }

    private static TableMeta loadTableMeta(Class<?> tableClass)
    {
        FlankerTable table = tableClass.getAnnotation(FlankerTable.class);
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName(StringUtils.isEmpty(table.name()) ? tableClass.getSimpleName() : table.name());
        tableMeta.setTableClass(tableClass);
        tableMeta.setCached(table.cached());
        Field[] fields = ReflectUtils.getAllFields(tableClass);
        List<ColumnMeta> columnList = new ArrayList<>();
        for (Field field : fields)
        {
            if (field.isAnnotationPresent(FlankerKey.class))
            {
                // 主键字段
                FlankerKey flankerKey = field.getAnnotation(FlankerKey.class);
                KeyMeta keyMeta = new KeyMeta();
                keyMeta.setField(field);
                keyMeta.setGeneratorType(flankerKey.generatorType());
                keyMeta.setName(StringUtils.isEmpty(flankerKey.name()) ? field.getName() : flankerKey.name());
                keyMeta.setLength(flankerKey.length());
                tableMeta.setKeyMeta(keyMeta);
            }
            else if (field.isAnnotationPresent(FlankerColumn.class))
            {
                // 常规字段
                FlankerColumn column = field.getAnnotation(FlankerColumn.class);
                ColumnMeta columnMeta = new ColumnMeta();
                columnMeta.setName(StringUtils.isEmpty(column.name()) ? field.getName() : column.name());
                columnMeta.setReadonly(!column.updatable());
                if (!column.updatable())
                {
                    // 只读字段必填
                    columnMeta.setNullable(false);
                }
                else
                {
                    columnMeta.setNullable(column.nullable());
                }
                Class<?> fieldType = field.getType();
                if (EnumSet.class.isAssignableFrom(fieldType))
                {
                    // 枚举集合类型
                    Class<? extends Enum<?>> enumType = column.enumType();
                    if (enumType == None.class)
                    {
                        System.out.println("error");
                    }
                    columnMeta.setEnumType(enumType);
                }
                SqlType sqlType = column.sqlType() != SqlType.VARCHAR ? column.sqlType() : DataUtils.toSqlType(fieldType);// 未指定类型时,根据属性类型推断字段类型
                columnMeta.setSqlType(sqlType);
                switch (sqlType)
                {
                    case VARCHAR:
                        columnMeta.setLength(column.length());
                        break;
                    case DECIMAL:
                        columnMeta.setPrecision(column.precision());
                        columnMeta.setScale(column.scale());
                        break;
                    default:
                        break;
                }
                columnMeta.setField(field);
                columnList.add(columnMeta);
            }
            else if (field.isAnnotationPresent(FlankerParent.class))
            {
                // 标识为上一级对象的属性
                tableMeta.setParentObjectField(field);
            }
        }
        if (tableMeta.getKeyMeta() == null)
        {
            // 无主键字段
        }
        tableMeta.setColumns(columnList.toArray(new ColumnMeta[0]));
        return tableMeta;
    }

    public static TableMeta getRootTableMeta(Class<?> tableClass)
    {
        if (tableMetaMap.containsKey(tableClass))
        {
            TableMeta tableMeta = tableMetaMap.get(tableClass);
            if (!tableMeta.isRoot())
            {
                // 非根节点
                throw new RuntimeException(FlankerErrorType.Metadata.TABLE_NOT_ROOT.message());
            }
            return tableMeta;
        }
        else
        {
            // 元数据不存在
            throw new RuntimeException(FlankerErrorType.Metadata.TABLE_NOT_EXISTS.message());
        }
    }
}
