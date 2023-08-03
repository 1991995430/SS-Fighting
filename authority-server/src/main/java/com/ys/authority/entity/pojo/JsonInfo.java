package com.ys.authority.entity.pojo;

import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
public class JsonInfo {
    public String id;
    public String path;
    public String name;
    public MenuMeta meta;
    public List<JsonInfo> children;
}


