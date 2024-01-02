package com.ss.song.service.impl;

import com.ss.song.meta.TableMeta;
import com.ss.song.model.IndexedData;
import com.ss.song.model.JdbcResource;
import com.ss.song.service.*;
import com.ss.song.utils.DataUtils;
import com.ss.song.utils.Query;
import com.ss.song.utils.QueryUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author shangsong 2023/12/25
 */
@Service
public class ApplicationServiceImpl implements ApplicationService {
    @Resource
    private FlankerMetaService metaService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private RelationService relationService;

    @Resource
    private FlankerTemplate flankerTemplate;

    @Resource
    private FlankerCache flankerCache;

    private Integer count = 99;

    @Override
    public void insertJdbcResource(JdbcResource resource) {
        TableMeta rootTableMeta = metaService.getRootTableMeta(resource.getClass());
        String rootKey = DataUtils.generateKey(rootTableMeta, resource);
        IndexedData rootIndexedData = new IndexedData(resource);
        Query insertQuery = QueryUtils.insertSql(rootTableMeta, rootIndexedData);
        List<Query> insertQueryList = new ArrayList<>();
        insertQueryList.add(insertQuery);
        if (!rootTableMeta.isLeaf())
        {
            DataUtils.insertChildren(rootIndexedData, rootKey, rootTableMeta, insertQueryList);
        }

        flankerCache.insertRoot(resource);

        relationService.insertRelation(rootTableMeta, resource, true, insertQueryList);

        for (Query query : insertQueryList)
        {
            this.executeUpdate(query);
            System.out.println();
        }
    }

    @Override
    public List<JdbcResource> getJdbcResourceList(String queryString) {
        return flankerTemplate.queryForList(JdbcResource.class,null);
    }

    @Override
    public void updateJdbcResource(String jdbcResourceId, JdbcResource resource) {
        flankerTemplate.setKey(resource, jdbcResourceId);
        flankerTemplate.overwrite(resource);
    }

    @Override
    public String tread() {
        count++;
        System.out.println(Thread.currentThread().getName());
        System.out.println("------");
        ApplicationServiceImpl applicationService = new ApplicationServiceImpl();
        System.out.println("count" + applicationService.count);
        return count.toString();
    }

    @Override
    public void deleteJdbcResource(String jdbcResourceId) {
        flankerTemplate.delete(JdbcResource.class, jdbcResourceId);
    }

    public void executeUpdate(Query query)
    {
        System.out.println("开始执行");
        jdbcTemplate.update(query.getSql(), query.getParams());
    }


}
