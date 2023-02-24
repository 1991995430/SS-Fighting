package com.workspace.apigateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workspace.apigateway.bean.AuthCheck;
import com.workspace.apigateway.utils.Constants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RefreshScope
@Component
@Slf4j
public class RequestGlobalFilter implements GlobalFilter, Ordered {
    /**
     * 不拦截的urL集合
     */
    @Value("#{'${write-list.urls:}'.empty ? null : '${write-list.urls:}'.split(',')}")
    private List<String> ignoreUrl;

    /**
     * 不拦截的IP集合
     */
    @Value("#{'${write-list.ips:}'.empty ? null : '${write-list.ips:}'.split(',')}")
    private List<String> ignoreIp;

    private final LoadBalancerClient loadBalancer;

    private RestTemplate restTemplate = new RestTemplate();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public RequestGlobalFilter(LoadBalancerClient loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestUrl = exchange.getRequest().getPath().toString();
        log.info("###[filter]Entry gateway filter. url: {}", requestUrl);

        if (isPassByWriteList(exchange, requestUrl)) {
            return chain.filter(exchange);
        }
        String token = exchange.getRequest().getHeaders().getFirst("access-token");
        if (Constants.isStringBlank(token)) {
            log.info("[filter]get token error. details is null");
            return packageNoTokenResponse(exchange);
        }
        ServerHttpResponse response = exchange.getResponse();
        return checkToken(exchange, chain, requestUrl, response, token);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private Mono<Void> checkToken(ServerWebExchange exchange, GatewayFilterChain chain, String requestUrl,
                                  ServerHttpResponse response, String token) throws JsonProcessingException, UnsupportedEncodingException {
        String url = loadBalancer.choose("authority-server").getUri().toString();
        url += "/authority/auth/checkAccess";
        AuthCheck req = new AuthCheck();
        req.setToken(token);
        req.setUrl(requestUrl);
        log.info(requestUrl + " send to " + url);
        String res = restTemplate.postForObject(url, req, String.class);
        log.info(requestUrl + " get response " + res);
        JsonNode node = objectMapper.readValue(res, JsonNode.class);
        int status = node.get("status").asInt();
        if (status != 200) {
            DataBuffer buffer = response.bufferFactory().wrap(res.getBytes(StandardCharsets.UTF_8));
            response.setStatusCode(HttpStatus.valueOf(status));
            response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
            log.info("[checkToken]token check error. details :" + res);
            return response.writeWith(Mono.just(buffer));
        }
        if (node.has("data")) {
            node = objectMapper.convertValue(node.get("data"), JsonNode.class);
            if (node.has("userId")) {
                return addResponseHeader(exchange, chain, node);
            }
            log.info("token check successful, but can not contain user id.");
        }
        return chain.filter(exchange);
    }

    private static final String httpHeadUserId = "userId";
    private static final String httpHeadUserInfo = "userInfo";
    private static final String httpHeadRoleInfo = "roleInfo";

    private static final String httpHeadRoleInfoList = "roleInfoList";
    private static final String ENC = "UTF-8";

    private Mono<Void> addResponseHeader(ServerWebExchange exchange, GatewayFilterChain chain, JsonNode node) throws UnsupportedEncodingException {
        Map<String, Object> addHeader = new HashMap<>();
        String userId = node.get(httpHeadUserId).asText();
        addHeader.put(httpHeadUserId, userId);
        if (node.has(httpHeadUserInfo)) {
            String userInfoStr = node.get(httpHeadUserInfo).asText();
            String httpStr = URLEncoder.encode(userInfoStr, ENC);
            addHeader.put(httpHeadUserInfo, httpStr);
        }
        if (node.has(httpHeadRoleInfo)) {
            String roleInfoStr = node.get(httpHeadRoleInfo).asText();
            String httpStr = URLEncoder.encode(roleInfoStr, ENC);
            addHeader.put(httpHeadRoleInfo, httpStr);
        }
        if (node.has(httpHeadRoleInfoList)) {
            String roleInfoListStr = node.get(httpHeadRoleInfoList).asText();
            String httpStr = URLEncoder.encode(roleInfoListStr, ENC);
            addHeader.put(httpHeadRoleInfoList, httpStr);
        }
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            for (Map.Entry<String, Object> entry : addHeader.entrySet()) {
                String value = String.valueOf(entry.getValue());
                log.info("[addResponseHeader]loop add key {}, value {}:", entry.getKey(), value);
                httpHeader.set(entry.getKey(), value);
            }
        };
        //将现有的request，添加当前身份
        ServerHttpRequest mutableReq = exchange.getRequest().mutate().headers(httpHeaders).build();
        ServerWebExchange mutableExchange = exchange.mutate().request(mutableReq).build();
        return chain.filter(mutableExchange);
    }

    private Mono<Void> packageNoTokenResponse(ServerWebExchange exchange) throws JsonProcessingException {
        ServerHttpResponse response = exchange.getResponse();
        Map<String, Object> message = new HashMap<>();
        message.put("code", 401);
        message.put("msg", "auth failed");
        message.put("timestamp", System.currentTimeMillis());
        byte[] bits = objectMapper.writeValueAsString(message).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "text/json;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }

    private boolean isPassByWriteList(ServerWebExchange exchange, String requestUrl) {
        if (urlListContains(ignoreUrl, requestUrl, exchange) != null) {
            log.info("[isPassByWriteList] URL <{}> passed by write url list.", requestUrl);
            return true;
        }
        if (ipListContains(ignoreIp, exchange) != null) {
            log.info("[isPassByWriteList] IP <{}> passed by write ip list", requestUrl);
            return true;
        }
        return false;
    }

    private String urlListContains(List<String> urlList, String url, ServerWebExchange exchange) {
        if (urlList == null) {
            return null;
        }
        // 命中白名单，直接返回
        for (String checkUrl : urlList) {
            if (url.startsWith(checkUrl)) {
                return checkUrl;
            }
        }
        return null;
    }

    private String ipListContains(List<String> ipList, ServerWebExchange exchange) {
        if (ipList == null) {
            return null;
        }

        if (exchange.getRequest().getRemoteAddress() == null) {
            return null;
        }
        String remoteIP = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        for (String checkIP : ipList) {
            if (remoteIP.equals(checkIP)) {
                return remoteIP;
            }
        }
        return null;
    }
}
