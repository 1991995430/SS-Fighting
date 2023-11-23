package com.ss.song.controller.Param;

import lombok.Data;

/**
 * author shangsong 2023/8/14
 */
@Data
public class ConSearchParams {

    private String id;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
