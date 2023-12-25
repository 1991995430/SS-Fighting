package com.ss.kfk.test;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;


/**
 * author shangsong 2023/11/29
 */
public class KafkaConsumerExample {
    public static void main(String[] args) {
        // 定义Kafka集群的地址
        String bootstrapServers = "192.168.3.180:9092";

        // 配置消费者的属性
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "test_group");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        // 创建消费者实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);

        try {
            // 订阅Topic
            String topic = "ss";
            consumer.subscribe(Arrays.asList(topic));

            // 消费消息
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, String> record : records) {
                    System.out.println("接收到消息: key = " + record.key() + ", value = " + record.value() +
                            ", 分区 = " + record.partition() + ", 偏移量 = " + record.offset());

                    // 手动提交偏移量
                    TopicPartition partition = new TopicPartition(record.topic(), record.partition());
                    OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(record.offset() + 1);
                    consumer.commitSync(Collections.singletonMap(partition, offsetAndMetadata));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }
}
