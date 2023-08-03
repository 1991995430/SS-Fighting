package com.ss.song.controller;
import com.ss.song.dto.*;
import com.ss.song.params.JobParams;
import io.trino.jdbc.$internal.okhttp3.Credentials;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@RestController
@RequestMapping("/v1/trino")
public class PracticeController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/con1")
    public List<DataDto> con1()  {
        /*Map<String, String> map = new HashMap<>();
        map.put("value", "value");
        map.put("text", "test");*/
        List<String> list = new ArrayList<>();
        list.add("ssss,111");
        list.add("1111111,222");
        list.add("22222,333");
        List<DataDto> list1 = new ArrayList<>();
        DataDto dataDto = new DataDto();
        dataDto.setText("ssss");
        dataDto.setValue("vcaa");
        DataDto dataDto1 = new DataDto();
        dataDto1.setValue("shang");
        dataDto1.setText("song");
        list1.add(dataDto1);
        list1.add(dataDto);
        return list1;
    }

    @PostMapping("/submit")
    public QueryResultVo submit(@RequestBody JobParams jobParams)  {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trino-User", "ss");
        // 提交任务到trino服务
        String nextUri = submitJobToTrino(headers, jobParams.getSql(), "8080");
        return getMetaData(nextUri);
    }

    private String submitJobToTrino(HttpHeaders headers, String trinoSql, String port) {
        HttpEntity<String> requestEntity = new HttpEntity<>(trinoSql, headers);
        // 获取当前主节点ip
        ResponseEntity<Map> exchange = restTemplate.exchange("http://localhost:8080/v1/statement", HttpMethod.POST, requestEntity, Map.class);
        return (String) exchange.getBody().get("nextUri");
    }


    public QueryResultVo getMetaData(String nextUri) {
        QueryResultVo queryResultVo = new QueryResultVo();
        List<Object> finalData = new ArrayList<>();
        while (nextUri != null) {
            QueryResponseInfo responseInfo = restTemplate.getForObject(nextUri, QueryResponseInfo.class);
            nextUri = responseInfo.getNextUri();
            QueryRespStats stats = responseInfo.getStats();

            // 获取字段名称
            List<Column> columnList = new ArrayList<>();
            if (responseInfo.getColumns() != null) {
                columnList = responseInfo.getColumns();
            }

            List<String> columnName = new LinkedList<>();
            columnList.forEach(columns -> columnName.add(columns.getName()));
            queryResultVo.setColumns(columnName);
            if (stats.getState().equals("FAILED")) {
                System.out.println("获取数据源信息任务查询失败！");
                throw new RuntimeException("数据源查询失败:" + ((Map<String, String>)responseInfo.getError()).get("message"));
            }

            if (responseInfo.getData() != null) {
                finalData.addAll((responseInfo.getData()));
            }
        }
        queryResultVo.setQueryResult(finalData);
        System.out.println("任务查询成功！");
        return queryResultVo;
    }

}
