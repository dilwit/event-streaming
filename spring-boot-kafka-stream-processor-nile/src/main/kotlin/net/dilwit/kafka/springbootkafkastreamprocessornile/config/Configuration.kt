package net.dilwit.kafka.springbootkafkastreamprocessornile.config

import com.maxmind.geoip2.DatabaseReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.nio.file.Paths

@Configuration
class Configuration {

    @Value("\${geoLocation.dbFilePath}")
    private lateinit var geoLocationDbFilePath: String

    @Bean
    fun databaseReader(): DatabaseReader {
        return DatabaseReader.Builder(File(Paths.get(geoLocationDbFilePath).toAbsolutePath().toString())).build()
    }
}