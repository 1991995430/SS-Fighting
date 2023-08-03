package com.ss.song.dto;

/**
 * author shangsong 2023/4/23
 */
public class Column {

    private String name;

    private String type;

    private Object typeSignature;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getTypeSignature() {
        return typeSignature;
    }

    public void setTypeSignature(Object typeSignature) {
        this.typeSignature = typeSignature;
    }
}
