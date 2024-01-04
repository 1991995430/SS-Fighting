package com.ss.song.test;

import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * author shangsong 2024/1/3
 */
public class CopyFileFromWindows {
    public static void main(String[] args) {

        String sourceFolderPath = "\\\\192.168.2.182\\ss";
        String destinationFolder = "D://copyFromWindows//";

        String username = "wl1712527524@163.com";
        String password = "wl960613";

        // 创建认证器并获取凭据
        Authenticator.setDefault(new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password.toCharArray());
            }
        });

        try {
            Path sourcePath = Paths.get(sourceFolderPath);
            Path destinationPath = Paths.get(destinationFolder);

            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = destinationPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.COPY_ATTRIBUTES);
                    return FileVisitResult.CONTINUE;
                }
            });

            System.out.println("文件拷贝成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
