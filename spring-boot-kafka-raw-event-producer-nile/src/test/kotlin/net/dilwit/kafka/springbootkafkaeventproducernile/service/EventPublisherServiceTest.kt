package net.dilwit.kafka.springbootkafkaeventproducernile.service

import Product
import RawEvent
import Shopper
import net.dilwit.kafka.springbootkafkaeventproducernile.service.EventPublisherService
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
class EventPublisherServiceTest {

    @MockBean
    private lateinit var kafkaTemplate: KafkaTemplate<String, RawEvent>

    @Autowired
    private lateinit var eventPublisherService: EventPublisherService

    @Test
    fun test_sendMessage_shouldSendMessage_whenRawEventProvided() {

        var rawEvent = getTestRawEvent()
        eventPublisherService.sendMessage(rawEvent)
        verify(kafkaTemplate, times(1)).send(any(), any())

    }

    private fun getTestRawEvent(): RawEvent {

        val shopper = Shopper(123, "Jane", "70.46.123.145")
        val product = Product("appl-001", "iPad")
        return RawEvent("SHOPPER_VIEWED_PRODUCT", shopper, product, "2018-10-15T12:01:35Z")
    }

}