package com.ss.song.test;

import com.ss.song.annotation.FlankerColumn;
import com.ss.song.annotation.FlankerKey;
import com.ss.song.annotation.FlankerParent;
import com.ss.song.annotation.FlankerTable;
import com.ss.song.enums.FlankerErrorType;
import com.ss.song.enums.None;
import com.ss.song.enums.SqlType;
import com.ss.song.meta.ColumnMeta;
import com.ss.song.meta.KeyMeta;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.User;
import com.ss.song.utils.ClassScanner;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.ReflectUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * author shangsong 2023/12/25
 */
public class Test1225 {
    private static final Map<Class<?>, TableMeta> tableMetaMap = new HashMap<>();

    public static void main(String[] args) {
        User user = new User();
        user.setId(12);
        user.setName("ss");
        user.setCity("nj");
        Class userClass = user.getClass();
        TableMeta tableMeta = new TableMeta();
        tableMeta.setName("user");
        tableMetaMap.put(userClass, tableMeta);

        if (tableMetaMap.containsKey(userClass)) {
            System.out.println("包含：" + tableMetaMap.get(userClass).getName());
        }
        ClassScanner.scan("com.ss.song").filter(clazz -> clazz.isAnnotationPresent(FlankerTable.class)).map(clazz -> loadTableMeta(clazz)).forEach(tableMeta1 -> tableMetaMap.put(tableMeta1.getTableClass(), tableMeta1));
        System.out.println(tableMetaMap);

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
}
