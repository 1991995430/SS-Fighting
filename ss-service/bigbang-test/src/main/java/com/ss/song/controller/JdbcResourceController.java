package com.ss.song.controller;

import com.ss.song.exception.ErrorUtils;
import com.ss.song.meta.TableMeta;
import com.ss.song.model.JdbcResource;
import com.ss.song.rest.RestResponse;
import com.ss.song.service.ApplicationService;
import com.ss.song.service.FlankerMetaService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/test")
public class JdbcResourceController {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private FlankerMetaService metaService;

    @Resource
    private ApplicationService applicationService;

    @PostMapping("/scan")
    public void con1() {
        final Set<String> tableNameSet = jdbcTemplate.queryForList("show tables", String.class).stream().map(name -> StringUtils.lowerCase(name)).collect(Collectors.toSet());
        System.out.println(tableNameSet);

        metaService.scan("com.ss.song");
    }

    @PostMapping("/get")
    public void con2() {
        Map<Class<?>, TableMeta> tableMetaMap = metaService.getTableMetaMap();
        System.out.println(tableMetaMap);
    }

    @RequestMapping(value = "/jdbcResource", method = RequestMethod.POST)
    @ApiOperation("添加JDBC资源")
    public RestResponse insertJdbcResource(@RequestBody JdbcResource resource)
    {
        RestResponse response = new RestResponse();
        try
        {
            applicationService.insertJdbcResource(resource);
        }
        catch (Exception e)
        {
            //log.error("insertJdbcResource failed!", e);
            ErrorUtils.write(e, response);
        }
        return response;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("获取JDBC资源列表")
    public List<JdbcResource> getJdbcResourceList(String queryString)
    {
        return applicationService.getJdbcResourceList(queryString);
    }
}
