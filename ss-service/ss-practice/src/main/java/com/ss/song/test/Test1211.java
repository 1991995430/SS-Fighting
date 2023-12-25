package com.ss.song.test;

import org.checkerframework.checker.units.qual.A;

/**
 * author shangsong 2023/12/11
 */
public class Test1211 {

    public static void main(String[] args) {

        Ab ab = new Ab();

        Thread t1 = new Thread(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "开始");
            synchronized (ab) {
                try {
                    ab.wait(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "结束");
        });

        Thread t2 = new Thread(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "开始");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "结束");
        });

        Thread t3 = new Thread(() -> {
            System.out.println("线程：" + Thread.currentThread().getName() + "开始");
            /*synchronized (ab) {
                ab.notify();
            }*/
            try {
                Thread.sleep(1000); // 添加延迟，给予 t1 获取锁并继续执行的机会
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("线程：" + Thread.currentThread().getName() + "结束");
        });
        t1.start();
        t2.start();
        t3.start();
    }

}
