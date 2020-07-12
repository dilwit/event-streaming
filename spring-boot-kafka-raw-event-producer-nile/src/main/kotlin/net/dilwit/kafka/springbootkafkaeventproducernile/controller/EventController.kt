package net.dilwit.kafka.springbootkafkaeventproducernile.controller

import RawEvent
import net.dilwit.kafka.springbootkafkaeventproducernile.service.EventPublisherService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value=["/events"])
class EventController {

    @Autowired private lateinit var eventPublisherService: EventPublisherService

    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE], produces = [MediaType.APPLICATION_JSON_VALUE])
    public fun publishRawEvent(@RequestBody rawEvent: RawEvent) {
        eventPublisherService.sendMessage(rawEvent)
    }
}



