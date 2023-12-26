package com.ss.song.test;

/**
 * author shangsong 2023/12/1
 */

public class ThreadLocalExample {
    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        Runnable runnable = () -> {
            threadLocal.set(Thread.currentThread().getName()); // 设置当前线程的数据
            System.out.println("ThreadLocal value in thread " + Thread.currentThread().getId() + ": " + threadLocal.get()); // 获取当前线程的数据
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();
    }
}

