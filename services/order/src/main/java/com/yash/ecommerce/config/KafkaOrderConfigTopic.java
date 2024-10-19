package com.yash.ecommerce.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaOrderConfigTopic {

    public NewTopic orderTopic(){
        return TopicBuilder
                .name("order-topic")
                .build();
    }
}
