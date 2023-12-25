package com.ss.song.test;

import com.ss.song.pojo.User;
import org.springframework.beans.BeanUtils;

/**
 * author shangsong 2023/12/1
 */
public class Test1201 {
    public static void main(String[] args) throws CloneNotSupportedException {

        User user1 = new User();
        user1.setName("ss123");
        User user2 = new User();
        BeanUtils.copyProperties(user1, user2);
        System.out.println(user2.getName());

        user1.setName("newName");
        System.out.println(user2.getName());

        User user3 = user1;

        System.out.println(user3.getName());

        user1.setName("qqqqq");

        System.out.println(user3.getName());


        User user4 = (User)user1.clone();
        System.out.println(user4.getName());
        user1.setName("pppppppppppp");
        System.out.println(user4.getName());

    }
}
