package com.ss.song.test;

import com.sun.org.apache.xpath.internal.operations.String;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFIleTest {

    public static void main(String[] args) throws IOException {

        File file = new File("D://doa_result.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        byte[] bytes = new byte[1024];
        int length = raf.read(bytes);
        System.out.println(length);

    }
}
