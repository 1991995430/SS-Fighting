package com.ss.song.test;

import org.checkerframework.checker.units.qual.A;

/**
 * author shangsong 2023/12/11
 */
public class Test1211 {

    public static void main(String[] args) {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我是新线程");
                System.out.println(Thread.currentThread().getName());
            }
        });
        t1.start();
    }
}
