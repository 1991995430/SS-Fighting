package com.ss.song.test;

import org.checkerframework.checker.units.qual.A;
import org.junit.Test;

import java.io.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FileAccessTest {
    public static void main(String[] args) throws IOException {
        File file = new File("D://doa_result.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        byte[] bytes = new byte[8 * 1024];
        int length = raf.read(bytes);
        System.out.println(length);
    }

    @Test
    public void testSingleTreadReadFile() throws IOException {

        File file = new File("D:\\QLDownload\\在暴雪时分\\在暴雪时分 第09集 4K(超高清SDR).qlv");
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream("D:\\xxx.qlv");
        byte[] bytes = new byte[8 * 1024];
        int len;
        long start = System.currentTimeMillis();
        while ((len = fis.read(bytes)) != -1) {
            fos.write(bytes, 0, len);
        }
        long end = System.currentTimeMillis();
        System.out.println("单线程复制共消耗了：" + (end - start));
    }

    @Test
    public void testMulTreadReadFile() throws IOException, InterruptedException {

        File file = new File("D:\\QLDownload\\在暴雪时分\\在暴雪时分 第09集 4K(超高清SDR).qlv");

        int threadNum = 5;

        long length = file.length();
        int part = (int) Math.ceil(length / threadNum);

        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < threadNum; i++) {
            final int j = i;
            Thread thread = new Thread(() -> {
                try {
                    RandomAccessFile in = new RandomAccessFile(file, "r");
                    RandomAccessFile out = new RandomAccessFile("D:\\mmm.qlv", "rw");
                    in.seek(j * part);
                    out.seek(j * part);

                    byte[] bytes = new byte[8 * 1024];
                    int len, plen = 0;
                    while(true) {
                        len = in.read(bytes);
                        if (len == -1) {
                            break;
                        }
                        plen += len;
                        out.write(bytes, 0, len);
                        if (plen >= part) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            thread.start();
            threadList.add(thread);
        }
        long start = System.currentTimeMillis();
        for (Thread thread : threadList) {
            thread.join();
        }
        long end = System.currentTimeMillis();
        System.out.println("单线程复制共消耗了：" + (end - start));
    }
}
