package com.ss.song.test;

import com.ss.song.meta.TableMeta;
import com.ss.song.model.User;
import com.ss.song.utils.ClassScanner;

import java.util.HashMap;
import java.util.Map;

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

        ClassScanner.scan("");

    }
}
