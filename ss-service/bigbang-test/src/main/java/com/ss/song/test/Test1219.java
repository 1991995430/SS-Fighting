package com.ss.song.test;

import com.ss.song.pojo.User;
import com.ss.song.service.UserService;

/**
 * author shangsong 2023/12/19
 */
public class Test1219 {
    static Integer count = 0;
    public static void main(String[] args) throws InterruptedException {

        UserService userService = new UserService();

        User user = new User();

        if (PermissionChecker.hasPermission(user, "admin")) {
            userService.adminPrivilege();
        } else {
            System.out.println("没有管理员权限");
        }

        if (PermissionChecker.hasPermission(user, "user")) {
            userService.userPrivilege();
        } else {
            System.out.println("没有普通用户权限");
        }

        userService.publicPrivilege();
    }

}
