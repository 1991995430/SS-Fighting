package com.ss.song.test;

/**
 * author shangsong 2023/12/1
 */

public class SynchronizedExample {
    /*private static Integer counter = 0;

    public synchronized static void increment() {
        counter++;
    }

    public static void main(String[] args) {
        Runnable runnable = () -> {
            for (int i = 0; i < 1000; i++) {
                increment(); // 对共享变量进行加一操作
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Counter value: " + counter); // 打印计数器的最终值
    }*/


    private static ThreadLocal<Integer> counter = ThreadLocal.withInitial(() -> 0);
    private static Integer totalCounter = 0;

    public static void increment() {
        counter.set(counter.get() + 1);
    }

    public static void main(String[] args) {
        Runnable runnable = () -> {
            try {
                for (int i = 0; i < 1000; i++) {
                    increment();
                }
            } finally {
                totalCounter += counter.get();
                counter.remove(); // 清除ThreadLocal变量
            }
        };

        Thread thread1 = new Thread(runnable);
        Thread thread2 = new Thread(runnable);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total Counter value: " + totalCounter); // 输出计数器的最终值
    }
}

