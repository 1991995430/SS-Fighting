package com.ss.song.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * author shangsong 2023/12/7
 */
public class Test1207 {

    public static void main(String[] args) {

        //ChannDataAA channDataAA = new CallbackImpl();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            System.out.println("before:" + list);
            list.add(i);
            System.out.println("after:" + list);
            //list.clear();
        }

    }

    //  接收回调函数的函数
    void  executeCallback(ChannDataAA  channDataAA)  {
        channDataAA.channelData(2,43,"dea");
    }

}
