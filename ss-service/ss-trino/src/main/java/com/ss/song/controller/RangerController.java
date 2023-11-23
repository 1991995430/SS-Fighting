package com.ss.song.controller;

import com.google.gson.Gson;
import com.ss.song.params.ServiceParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * author shangsong 2023/10/7
 */
@Slf4j
@RestController
@RequestMapping("/v1/ranger")
public class RangerController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/createService")
    public void create(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("X-XSRF_HEADER", "\"\"");
        headers.set("Content-Type", "application/json");
        String username = "admin";
        String password = "admin";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // 添加身份验证头部
        headers.set("Authorization", "Basic " + encodedCredentials);

        ServiceParam service = new ServiceParam();
        service.setName("servic1234");
        service.setType("trino");
        service.setEnabled(true);
        Map<String, String> configMap = new HashMap<>();
        configMap.put("username", "admin");
        configMap.put("jdbc.driverClassName", "io.datanet.jdbc.DataNetDriver");
        configMap.put("jdbc.url", "jdbc:datanet://192.168.3.204:10000");
        service.setConfigs(configMap);
        Gson gson = new Gson();
        String json = gson.toJson(service);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<Map> exchange = restTemplate.exchange("http://localhost:8989/ranger/service/plugins/services", HttpMethod.POST, requestEntity, Map.class);
        if (exchange.getBody() != null) {
            int a = (Integer)exchange.getBody().get("id");
            System.out.println("aaaa:" + a);
            System.out.println("service id :" + exchange.getBody().get("id"));
        }
    }

    @GetMapping("/deleteService")
    public void delete(@RequestParam (value = "id") Integer id, HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("X-XSRF_HEADER", "\"\"");
        headers.set("Content-Type", "application/json");
        String username = "admin";
        String password = "admin";
        String credentials = username + ":" + password;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

        // 添加身份验证头部
        headers.set("Authorization", "Basic " + encodedCredentials);
        ServiceParam service = new ServiceParam();
        Gson gson = new Gson();
        String json = gson.toJson(service);
        HttpEntity<String> requestEntity = new HttpEntity<>(json, headers);
        ResponseEntity<String> exchange = restTemplate.exchange("http://192.168.3.23:6083/service/plugins/services/" + id, HttpMethod.DELETE, requestEntity, String.class);
    }

}
