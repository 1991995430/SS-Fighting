package com.ss.song.test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * author shangsong 2023/11/30
 */
public class TestClassLoad extends ClassLoader{
    private String classPath;

    public TestClassLoad(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        try {
            byte[] classData = loadClaseeData(className);
            return defineClass(className, classData, 0, classData.length);
        } catch (IOException ex) {
            throw new ClassNotFoundException("Failed to load class " + className, ex);
        }
    }

    private byte[] loadClaseeData(String className) throws IOException {
        String fileName = className.replace(".", "/") + ".class";
        try (FileInputStream fis = new FileInputStream(classPath + "/" + fileName);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }

    public static void main(String[] args) throws Exception {
        String classPath = "D:\\Project\\SS-Fighting\\ss-service\\ss-trino\\target\\classes";
        TestClassLoad testClassLoad = new TestClassLoad(classPath);
        Class<?> loadClass = testClassLoad.loadClass("com.ss.song.test.TestClass");
        Object o = loadClass.newInstance();
        System.out.println(o instanceof TestClass);
        if (o instanceof TestClass) {
            TestClass testClass = (TestClass) o;
            System.out.println(testClass.getName());
        }
    }
}
