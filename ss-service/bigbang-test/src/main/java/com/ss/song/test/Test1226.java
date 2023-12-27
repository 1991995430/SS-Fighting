package com.ss.song.test;

import com.ss.song.model.JdbcResource;
import com.ss.song.pojo.User;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * author shangsong 2023/12/26
 */
public class Test1226 {
    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException {

        long copyStartTime = System.currentTimeMillis();
        User sourceUser = new User("sunyangwei");
        User targetUser = new User();
        for(int i = 0; i < 10000; i++) {
            BeanUtils.copyProperties(sourceUser, targetUser);
        }
        System.out.println("copy方式："+(System.currentTimeMillis()-copyStartTime));

        long setStartTime = System.currentTimeMillis();
        for(int i = 0; i < 10000; i++) {
            //targetUser.setUserName(sourceUser.getUserName());
        }
        System.out.println("set方式："+(System.currentTimeMillis()-setStartTime));
    }
}
