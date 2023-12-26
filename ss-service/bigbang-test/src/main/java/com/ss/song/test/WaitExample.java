package com.ss.song.test;

/**
 * author shangsong 2023/12/1
 */
public class WaitExample {
    public static void main(String[] args) {

        final Object lock = new Object();
        System.out.println("--start--");

        Thread t1 = new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println("t1 : before wait");
                    lock.wait();
                    System.out.println("t1: after wait");
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(()->{
           synchronized (lock) {
               System.out.println("t2 :before notify");
               lock.notify();
               System.out.println("t2:after notify");
           }
        });
        t1.start();
        try {
            Thread.sleep(1000); // 等待1秒，确保线程1先执行
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t2.start();

        System.out.println("end");
    }

}
