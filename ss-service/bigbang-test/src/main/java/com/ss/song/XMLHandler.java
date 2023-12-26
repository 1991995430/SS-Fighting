package com.ss.song;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * author shangsong 2023/12/20
 */
public class XMLHandler extends DefaultHandler {

    // 开始标签
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        // 判断是否为参数节点
        //if (qName.equalsIgnoreCase("param")) {
        if (qName.equalsIgnoreCase("param")) {
            // 获取参数名和参数值
            String paramName = attributes.getValue("name");
            // 输出参数名
            System.out.print(paramName + ": ");
        }
    }

    // 结束标签
    public void endElement(String uri, String localName, String qName) throws SAXException {
        // 输出换行
        System.out.print(" :::"+ qName);
    }

    // 字符串内容
    public void characters(char ch[], int start, int length) throws SAXException {
        // 输出参数值
        System.out.print(new String(ch, start, length));
    }
}
