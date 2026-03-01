package com.voidex.thoth.consumers;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

    @KafkaListener(
            topics = "${thoth.kafka.topics.message.name}",
            groupId = "${thoth.kafka.topics.message.group-id}",
            concurrency = "${thoth.kafka.topics.message.concurrency}"
    )
    public void listenMessage(ConsumerRecord<String, String> topicMessage, Acknowledgment acknowledgment){
        try{

        } finally{
            acknowledgment.acknowledge();
        }
    }
}
