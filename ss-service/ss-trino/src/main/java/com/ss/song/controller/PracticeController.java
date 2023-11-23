package com.ss.song.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ss.song.controller.Param.ConSearchParams;
import com.ss.song.dto.*;
import com.ss.song.mapper.DataDtoMapper;
import com.ss.song.mapper.dao.DataDto;
import com.ss.song.params.JobParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@RequestMapping("/v1/trino")
public class PracticeController {

    @Autowired
    private HttpServletRequest request;

    private final RestTemplate restTemplate = new RestTemplate();

    @Resource
    private DataDtoMapper dataDtoMapper;

    @PostMapping("/con1")
    public List<Object> con1(@RequestBody ConSearchParams conSearchParams, HttpServletRequest request) {
        System.out.println(request.getHeader("username"));
        System.out.println(request.getHeader("password"));

        QueryWrapper<DataDto> dataDtoQueryWrapper = new QueryWrapper<>();
        List<DataDto> dataDtos = dataDtoMapper.selectList(dataDtoQueryWrapper);
        List<Object> list = new ArrayList<>(dataDtos);

        return list;
        //return "1,shang,18\n2,ss1,12\n12,hao,123";
    }

    @GetMapping("/con3")
    public String con3(HttpServletRequest request) {

        System.out.println(request.getHeader("username"));
        System.out.println(request.getHeader("password"));

        return "1,shang,18,28.3\n2,ss1,12,12.36\n12,hao,123,12.28";
    }

    @PostMapping("/con2")
    public ResponseEntity<InputStreamResource> exportData(HttpServletRequest request) throws IOException {

        System.out.println(request.getHeader("username"));
        System.out.println(request.getHeader("password"));

        StringBuilder data = new StringBuilder();
        data.append("8,ss,3,2.6\n");
        data.append("9,ss1,3,2.6\n");

        // 写入文件
        File file = writeDataToFile(data.toString());

        // 生成文件流
        Path filePath = file.toPath();
        InputStreamResource resource = new InputStreamResource(Files.newInputStream(filePath));

        // 返回文件流
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping("/con4")
    public ResponseEntity<Object> con4(HttpServletRequest request) throws IOException {

        System.out.println(request.getHeader("username"));
        System.out.println(request.getHeader("password"));

        List<Object> list1 = new ArrayList<>();
        DataSDto dataDto = new DataSDto();
        dataDto.setClerk("ss");
        dataDto.setCustkey(123);
        dataDto.setOrderpriority("orderP");
        dataDto.setShippriority(26);
        dataDto.setTotalprice(56.32);
        dataDto.setOrderkey(123);
        DataSDto dataDto1 = new DataSDto();
        dataDto1.setClerk("ss111");
        dataDto1.setCustkey(321);
        dataDto1.setOrderpriority("orderP11");
        dataDto1.setShippriority(32);
        dataDto1.setTotalprice(46.32);
        dataDto1.setOrderkey(21);
        list1.add(dataDto1);
        list1.add(dataDto);

        // 返回文件流
        return ResponseEntity.ok().body(list1);
    }

    @PostMapping("/con5")
    public String con5(HttpServletRequest request) {

        System.out.println(request.getHeader("username"));
        System.out.println(request.getHeader("password"));

        List<Object> list1 = new ArrayList<>();
        DataSDto dataDto = new DataSDto();
        dataDto.setClerk("ss");
        dataDto.setCustkey(123);
        dataDto.setOrderpriority("orderP");
        dataDto.setShippriority(26);
        dataDto.setTotalprice(56.32);
        dataDto.setOrderkey(123);
        dataDto.setOrderstatus("正常");
        DataSDto dataDto1 = new DataSDto();
        dataDto1.setClerk("ss111");
        dataDto1.setCustkey(321);
        dataDto1.setOrderpriority("orderP11");
        dataDto1.setShippriority(32);
        dataDto1.setTotalprice(46.32);
        dataDto1.setOrderkey(21);
        dataDto1.setOrderstatus("失败");
        list1.add(dataDto1);
        list1.add(dataDto);
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(list1);

        return json;
    }

    private File writeDataToFile(String data) throws IOException {
        File file = new File("data.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(data);
        }

        return file;
    }


    public static void main(String[] args) {
        System.out.println(new Time(new Date().getTime()));
    }

    @PostMapping("/submit")
    public QueryResultVo submit(@RequestBody JobParams jobParams) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Trino-User", "admin");
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
                throw new RuntimeException("数据源查询失败");
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
