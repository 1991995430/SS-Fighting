package com.ss.song.test;

import com.ss.song.XMLHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * author shangsong 2023/12/20
 */
public class ParseXmlFile {
    public static void main(String[] args) {
        try {
            // 创建解析器工厂
            SAXParserFactory factory = SAXParserFactory.newInstance();
            // 创建解析器
            SAXParser parser = factory.newSAXParser();
            // 解析xml文件
            parser.parse("D:\\Project\\SS-Fighting\\ss-service\\ss-trino\\test.xml", new XMLHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
