package com.ss.song.service;

import com.ss.song.xml.XmlCluster;
import com.ss.song.xml.XmlNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * author shangsong 2024/1/4
 */
@Service
public class AsyncService {

    @Resource
    private TaskExecutor taskExecutor;

    @Resource
    private XmlCluster xmlCluster;

    @Autowired
    @Qualifier("sim2Impl")
    private MutInter mutInter;

    //@Async
    public void aa() {
        System.out.println("我是线程aa");
        System.out.println(taskExecutor.getClass().getName());
        System.out.println(Thread.currentThread().getName());
        mutInter.bb();

        System.out.println("------------");
        List<XmlNode> nodeList = xmlCluster.getNodeList();
        System.out.println(nodeList);
    }
}
