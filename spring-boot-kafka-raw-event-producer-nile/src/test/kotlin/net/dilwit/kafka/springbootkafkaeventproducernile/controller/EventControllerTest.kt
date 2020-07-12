package net.dilwit.kafka.springbootkafkaeventproducernile.controller

import Product
import RawEvent
import Shopper
import com.google.gson.Gson
import net.dilwit.kafka.springbootkafkaeventproducernile.controller.EventController
import net.dilwit.kafka.springbootkafkaeventproducernile.service.EventPublisherService
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@RunWith(SpringRunner::class)
@WebMvcTest(EventController::class)
class EventControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var gson: Gson

    @MockBean
    private lateinit var eventPublisherService: EventPublisherService

    @Test
    fun test_publishRawEvent_shouldReturnOk_WhenRawEventIsProvided() {

        val rawEvent = getTestRawEvent()
        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                .content(gson.toJson(rawEvent))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist())
    }

    private fun getTestRawEvent(): RawEvent {

        val shopper = Shopper(123, "Jane", "70.46.123.145")
        val product = Product("appl-001", "iPad")
        return RawEvent("SHOPPER_VIEWED_PRODUCT", shopper, product, "2018-10-15T12:01:35Z")
    }

}