package net.dilwit.kafka.springbootkafkastreamprocessornile

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication(exclude = [JacksonAutoConfiguration::class])
class SpringBootKafkaStreamProcessorNileApplication

fun main(args: Array<String>) {
	runApplication<SpringBootKafkaStreamProcessorNileApplication>(*args)
}