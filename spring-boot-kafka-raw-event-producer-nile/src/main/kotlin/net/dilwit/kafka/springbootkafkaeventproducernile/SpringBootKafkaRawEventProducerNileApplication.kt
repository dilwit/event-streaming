package net.dilwit.kafka.springbootkafkaeventproducernile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [JacksonAutoConfiguration::class])
class SpringBootKafkaRawEventProducerNileApplication

fun main(args: Array<String>) {
    runApplication<SpringBootKafkaRawEventProducerNileApplication>(*args)
}
