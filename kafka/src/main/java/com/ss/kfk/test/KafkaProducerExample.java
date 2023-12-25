package com.ss.kfk.test;

/**
 * author shangsong 2023/11/29
 */

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.*;

import java.util.Properties;
import java.util.function.Consumer;

public class KafkaProducerExample {

    public static void main(String[] args) {

        // 配置生产者属性
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.3.180:9092,192.168.3.181:9092,192.168.3.182:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "all");
        props.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 60000);
        // 设置重试次数
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        // 设置重试间隔时间
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        // 创建生产者实例
        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 1000; i++) {
            // 创建并发送消息
            String topic = "ss";
            String key = "key1";
            String value = "Hello, MMMM!" + i;
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        exception.printStackTrace();
                    } else {
                        System.out.println("Produced message: topic = " + metadata.topic() +
                                ", partition = " + metadata.partition() +
                                ", offset = " + metadata.offset());
                    }
                }
            });
        }

        // 关闭生产者
        producer.close();
    }

}
