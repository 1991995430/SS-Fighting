package com.ss.song.service;

import com.ss.song.annotation.Permission;
import com.ss.song.annotation.SsLog;

/**
 * author shangsong 2023/12/19
 */
public class UserService {
    @Permission("admin")
    public  void  adminPrivilege()  {
        System.out.println("管理员权限执行的操作");
    }

    @Permission("user")
    public  void  userPrivilege()  {
        System.out.println("普通用户权限执行的操作");
    }

    @SsLog("publicPrivilege exec Log")
    public  void  publicPrivilege()  {
        System.out.println("公共权限执行的操作");
    }
}
