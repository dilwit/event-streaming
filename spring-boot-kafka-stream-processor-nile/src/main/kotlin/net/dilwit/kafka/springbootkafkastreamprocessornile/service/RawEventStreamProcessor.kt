package net.dilwit.kafka.springbootkafkastreamprocessornile.service

import EnrichedEvent
import EnrichedShopper
import ErrorEvent
import GeoLocation
import RawEvent
import com.google.gson.Gson
import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.model.CityResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.messaging.MessageHeaders
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.net.InetAddress

@Service
class RawEventStreamProcessor {

    private val logger: Logger = LoggerFactory.getLogger(RawEventStreamProcessor::class.java)

    @Autowired
    private lateinit var gson: Gson

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    private lateinit var databaseReader: DatabaseReader

    @Value("\${kafka.topic.enrichedEvents}")
    private lateinit var enrichedEventsTopic: String

    @Value("\${kafka.topic.errorEvents}")
    private lateinit var badEventsTopic: String

    @KafkaListener(topics = ["\${kafka.topic.rawEvents}"])
    fun readRawEvents(@Payload rawEvent: String, @Headers messageHeaders: MessageHeaders) {
        logger.info("Reading from raw-events topic: $rawEvent")
        processRawEvent(rawEvent)
    }

    private fun processRawEvent(rawEventString: String) {
        // Deserialize
        val rawEvent: RawEvent = gson.fromJson(rawEventString, RawEvent::class.java)

        // Validate raw-event
        var errors: MutableList<String> = validate(rawEvent)

        if(errors.isEmpty()) {
            // Validate ip with geoIp db
            val cityResponse:CityResponse = databaseReader.city(InetAddress.getByName(rawEvent.shopper.ipAddress))
            if (cityResponse != null) {
                // If no validation errors, send it to enriched-events topic
                kafkaTemplate.send(enrichedEventsTopic, gson.toJson(getEnrichedEvent(rawEvent, cityResponse)))
                return
            } else
                errors.add("Validation Error: GeoLocation is not found")
        }

        // Any validation errors, send it to bad-events topic
        kafkaTemplate.send(badEventsTopic, gson.toJson(getErrorEvent(rawEvent, errors)))
    }

    private fun validate(rawEvent: RawEvent): MutableList<String> {
        var errors: MutableList<String> = ArrayList()

        if(StringUtils.isEmpty(rawEvent.shopper.ipAddress))
            errors.add("Validation Error: IP address is not found")

        return errors
    }

    private fun getErrorEvent(rawEvent: RawEvent, errors: List<String>): ErrorEvent {
        return ErrorEvent(errors, rawEvent)
    }

    private fun getEnrichedEvent(rawEvent: RawEvent, cityResponse: CityResponse): EnrichedEvent {
        val shopper = rawEvent.shopper
        return EnrichedEvent(rawEvent.event, EnrichedShopper(shopper.id, shopper.name, shopper.ipAddress, GeoLocation(cityResponse.city.name, cityResponse.country.name)), rawEvent.product, rawEvent.timestamp)
    }
}