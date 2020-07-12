package net.dilwit.kafka.springbootkafkaeventproducernile.service

import RawEvent
import com.google.gson.Gson
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Service

@Service
class EventPublisherService {

    private val logger: Logger = LoggerFactory.getLogger(EventPublisherService::class.java)

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var gson: Gson

    @Value("\${kafka.topic.rawEvents}")
    private lateinit var topic: String;


    fun sendMessage(rawEvent: RawEvent) {
        val jsonString = gson.toJson(rawEvent)
        logger.info("Sending rawEvent to topic: $jsonString")
        kafkaTemplate.send(topic, jsonString);
    }
}
