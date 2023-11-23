package com.ss.song.datasource;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DataSourceFactory {
    private static Map<String, Class<?>> dsClassMap = new HashMap<>();
    static {
        dsClassMap.put(DataSourceDaMeng.TYPE, DataSourceDaMeng.class);
        dsClassMap.put(DataSourceOracle.TYPE, DataSourceOracle.class);
        dsClassMap.put(DataSourceKingBase.TYPE, DataSourceKingBase.class);
        dsClassMap.put(DataSourceGBase.TYPE, DataSourceGBase.class);
        dsClassMap.put(DataSourceGBase8a.TYPE, DataSourceGBase8a.class);
        dsClassMap.put(DataSourceGBase8s.TYPE, DataSourceGBase8s.class);
        dsClassMap.put(DataSourceMySql.TYPE, DataSourceMySql.class);
        dsClassMap.put(DataSourceHive.TYPE, DataSourceHive.class);
        dsClassMap.put(DataSourceSQLserver.TYPE, DataSourceSQLserver.class);
        dsClassMap.put(DataSourcePostgresql.TYPE, DataSourcePostgresql.class);
        dsClassMap.put(DataSourceGreenPlum.TYPE, DataSourceGreenPlum.class);
    }

    public static DataSource create(DataSourceParam param) {
        Class<?> clazz = dsClassMap.get(param.getType());
        if (clazz == null) {
            //log.error("[create]Fail to find data source type: {}", param.getType());
            return null;
        }
        try {
            return (DataSource) clazz.getDeclaredConstructor(DataSourceParam.class).newInstance(param);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //log.error("[create]Fail to create data source instance for no matched method");
            return null;
        }
    }
}
