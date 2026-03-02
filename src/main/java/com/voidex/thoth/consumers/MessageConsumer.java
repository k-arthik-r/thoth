package com.voidex.thoth.consumers;

import com.voidex.thoth.dto.Email;
import com.voidex.thoth.service.EmailNotificationCollector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
public class MessageConsumer {

    private final EmailNotificationCollector emailNotificationCollector;
    private final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);
    private final ObjectMapper mapper = new ObjectMapper();

    MessageConsumer(EmailNotificationCollector emailNotificationCollector){
        this.emailNotificationCollector = emailNotificationCollector;
    }

    @KafkaListener(
            topics = "${thoth.kafka.topics.message.name}",
            groupId = "${thoth.kafka.topics.message.group-id}",
            concurrency = "${thoth.kafka.topics.message.concurrency}"
    )
    public void listenMessage(ConsumerRecord<String, String> topicMessage, Acknowledgment acknowledgment){
        try{
            LOG.info("Processing incoming email notification: {}", topicMessage.toString());

            Email email = mapper.readValue(topicMessage.value(), Email.class);

            emailNotificationCollector.processEmailNotification(email);

        } finally{
            acknowledgment.acknowledge();
        }
    }
}
