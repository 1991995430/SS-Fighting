package com.ss.kfk.test;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * author shangsong 2023/11/27
 */
public class KafkaTest {

    private static final String GROUPID = "codingce_consumer_a";

    public static void main(String[] args) {

        Properties p = new Properties();
        // bootstrap.servers: kafka的地址, 多个地址用逗号分割
        p.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.31.150:9092");
        // 消费者所属的分组id, 组名 不同组名可以重复消费.例如你先使用了组名A消费了Kafka的1000条数据, 但是你还想再次进行消费这1000条数据,
        // 并且不想重新去产生, 那么这里你只需要更改组名就可以重复消费了.
        p.put(ConsumerConfig.GROUP_ID_CONFIG, GROUPID);
        // 是否自动提交, 默认为true.
        p.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 从poll(拉)的回话处理时长
        p.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        // 超时时间
        p.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        // 一次最大拉取的条数
        p.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 1000);
        // 消费规则, 默认earliest
        p.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // key.serializer: 键序列化, 默认org.apache.kafka.common.serialization.StringDeserializer.
        p.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // value.deserializer:值序列化, 默认org.apache.kafka.common.serialization.StringDeserializer.
        p.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(p);
        // 订阅消息
        //kafkaConsumer.subscribe(Collections.singletonList(Producer.topic));
        do {
            // 订阅之后, 再从kafka中拉取数据
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("-----topic:%s, offset:%d, 消息:%s-----\n", record.topic(), record.offset(), record.value());
            }
        } while (true);

    }

}
