package com.ss.song.test;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class VolatileTest {
    volatile boolean isInit;
    @Test
    public void aa() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            String treadName = Thread.currentThread().getName();
            for (int i = 0; i < 1000; i++) {
                if (i == 501) {
                    System.out.println("线程："+ treadName + "=》初始化完成");
                    isInit = true;
                    break;
                } else {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        Thread t2 = new Thread(() -> {
            String treadName = Thread.currentThread().getName();
            // System.out.println(Thread.currentThread().getName());
            System.out.println("线程：" + treadName + "=》监控开始，此时状态：" +isInit);
            while(!isInit) {
                try {
                    System.out.println("线程：" + treadName + "=》初始化未完成，进入等待。。。");
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("线程：" + treadName + "=》监控结束，此时状态：" +isInit);
            System.out.println("线程：" + treadName + "=》我执行完了");
        });
        Map<Integer, String> map1 = new HashMap<>();
        map1.put(1, "");
        Map<Integer, String> map2 = new ConcurrentHashMap<>();
        map2.put(1, "");

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }
}
