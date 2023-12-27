package com.ss.song.pojo;

import com.ss.song.annotation.Permission;

/**
 * author shangsong 2023/12/1
 */
public class User implements  Cloneable{
    private Integer id;
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public  Object  clone()  throws  CloneNotSupportedException  {
        User  newObj  =  (User)  super.clone();
        //  复制非静态字段
        newObj.copyFields(this);
        return  newObj;
    }

    @Permission("admin")
    private  void  copyFields(User  source)  {
        //  复制非静态字段
        this.id  =  source.id;
        this.name  =  source.name;
    }
}
