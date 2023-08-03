package com.ss.song.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class yamlgenerateUtils {

    /*public static void main(String[] args) throws IOException {
        // create data structure
        Map<String, Object> compose = new LinkedHashMap<>();
        compose.put("version", "3");

        Map<String, Object> services = new LinkedHashMap<>();

        Map<String, Object> coordinator = new LinkedHashMap<>();
        coordinator.put("image", "trino-image");
        coordinator.put("container_name", "coordinator");
        coordinator.put("hostname", "coordinator");
        // db.put("environment", Arrays.asList("MYSQL_ROOT_PASSWORD=my-secret-pw"));

        Map<String, Object> worker01 = new LinkedHashMap<>();
        worker01.put("image", "trino-image");
        worker01.put("container_name", "worker01");
        worker01.put("hostname", "worker01");

        services.put("Coordinator", coordinator);
        services.put("Worker01", worker01);
        compose.put("services", services);

        // create YAML
        Yaml yaml = new Yaml();
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);

        // write to file
        FileWriter writer = new FileWriter("D:\\docker-compose.yml");
        yaml.dump(compose, writer);
    }*/

    static class Config {
        private String version;
        private Map<String, Object> settings;

        public Config(String version) {
            this.version = version;
            this.settings = new HashMap<>();
        }

        public void setSetting(String key, Object value) {
            settings.put(key, value);
        }
    }
    public static void main(String[] args) throws IOException {
        Config config = new Config("3.0");
        config.setSetting("debug", true);
        config.setSetting("port", 8080);

        ObjectMapper mapper = new ObjectMapper();
        String yml = mapper.writeValueAsString(config);

        /*ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        String yml = mapper.writeValueAsString(config);

        // 格式化yml
        String formattedYml = new YAMLMapper().writeValueAsString(
                new YAMLMapper().readValue(yml, Object.class));*/

        // 将文件保存
        File file = new File("D:\\config.yml");
        //FileUtils.writeStringToFile(file, formattedYml, "UTF-8");
    }

}
