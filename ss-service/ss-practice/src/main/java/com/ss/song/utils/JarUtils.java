package com.ss.song.utils;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * author shangsong 2023/12/22
 */
public class JarUtils {

    private static void loadJarList(File dir, List<URL> list, boolean recursive)
    {
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (recursive && file.isDirectory())
                {
                    // 递归加载子目录中的jar包
                    loadJarList(file, list, true);
                }
                else if (file.isFile())
                {
                    String fileName = file.getName().toLowerCase();
                    if (fileName.endsWith(".jar"))
                    {
                        try
                        {
                            URL url = file.toURI().toURL();
                            list.add(url);
                        }
                        catch (MalformedURLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
