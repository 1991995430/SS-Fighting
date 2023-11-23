package com.ss.song.test;

import com.google.gson.Gson;
import com.ss.song.params.ServiceParam;
import com.ss.song.servie.TestService;
import org.apache.poi.ss.formula.functions.T;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * author shangsong 2023/10/12
 */
public class Test1012 {

    public static void main(String[] args) {

        Client client = null;
        client = Client.create();
        ServiceParam service = new ServiceParam();
        service.setName("service111222");
        service.setType("trino");
        service.setEnabled(true);
        Map<String, String> configMap = new HashMap<>();
        configMap.put("username", "admin");
        configMap.put("jdbc.driverClassName", "io.datanet.jdbc.DataNetDriver");
        configMap.put("jdbc.url", "jdbc:datanet://192.168.3.204:10000");
        service.setConfigs(configMap);
        Gson gson = new Gson();
        String json = gson.toJson(service);
        WebResource webResource = client.resource("http://192.168.3.23:6083/service/plugins/services");
        ClientResponse clientResponse = webResource.accept("").get(ClientResponse.class);
    }

}
