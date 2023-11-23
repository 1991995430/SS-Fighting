package com.ss.song.datasource;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Record {
    public Map<String, Field> fieldMap = new HashMap<>();
    public List<Field> fieldList = new ArrayList<>();

    public void add(Field f) {
        fieldList.add(f);
        fieldMap.put(f.name, f);
    }

    public Field get(String fieldName) {
        return fieldMap.get(fieldName);
    }

    public Map<String, Field> getFieldMap() {
        return fieldMap;
    }

    public void setFieldMap(Map<String, Field> fieldMap) {
        this.fieldMap = fieldMap;
    }

    public List<Field> getFieldList() {
        return fieldList;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
