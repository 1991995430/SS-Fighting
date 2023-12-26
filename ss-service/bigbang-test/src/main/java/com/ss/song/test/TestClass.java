package com.ss.song.test;

/**
 * author shangsong 2023/11/30
 */
public class TestClass {
    private String id;

    private String name;

    public static final String PPP = "PPP";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name + PPP;
    }

    public void setName(String name) {
        this.name = name;
    }
}
