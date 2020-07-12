package net.dilwit.samza.highlevelapi.service;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.apache.samza.application.StreamApplication;
import org.apache.samza.application.descriptors.StreamApplicationDescriptor;
import org.apache.samza.operators.KV;
import org.apache.samza.operators.MessageStream;
import org.apache.samza.operators.OutputStream;
import org.apache.samza.operators.windows.Windows;
import org.apache.samza.serializers.IntegerSerde;
import org.apache.samza.serializers.KVSerde;
import org.apache.samza.serializers.StringSerde;
import org.apache.samza.system.kafka.descriptors.KafkaInputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaOutputDescriptor;
import org.apache.samza.system.kafka.descriptors.KafkaSystemDescriptor;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ErrorEventsCounterStreamProcessor implements StreamApplication {

    private static final String KAFKA_SYSTEM_NAME = "kafka";
    private static final List<String> KAFKA_CONSUMER_ZK_CONNECT = ImmutableList.of("localhost:2181");
    private static final List<String> KAFKA_PRODUCER_BOOTSTRAP_SERVERS = ImmutableList.of("localhost:9092");
    private static final Map<String, String> KAFKA_DEFAULT_STREAM_CONFIGS = ImmutableMap.of("replication.factor", "1");

    private static final String INPUT_STREAM_ID = "error-events";
    private static final String OUTPUT_STREAM_ID = "one-minute-error-events-counter";

    @Override
    public void describe(StreamApplicationDescriptor appDescriptor) {

        // Create a KafkaSystemDescriptor providing properties of the cluster
        KafkaSystemDescriptor kafkaSystemDescriptor = new KafkaSystemDescriptor(KAFKA_SYSTEM_NAME)
                .withConsumerZkConnect(KAFKA_CONSUMER_ZK_CONNECT)
                .withProducerBootstrapServers(KAFKA_PRODUCER_BOOTSTRAP_SERVERS)
                .withDefaultStreamConfigs(KAFKA_DEFAULT_STREAM_CONFIGS);

        // For each input or output stream, create a KafkaInput/Output descriptor
        KafkaInputDescriptor<KV<String, String>> inputDescriptor =
                kafkaSystemDescriptor.getInputDescriptor(INPUT_STREAM_ID,
                        KVSerde.of(new StringSerde(), new StringSerde()));
        KafkaOutputDescriptor<KV<String, String>> outputDescriptor =
                kafkaSystemDescriptor.getOutputDescriptor(OUTPUT_STREAM_ID,
                        KVSerde.of(new StringSerde(), new StringSerde()));

        // Obtain a handle to a MessageStream that you can chain operations on
        MessageStream<KV<String, String>> errorEvents = appDescriptor.getInputStream(inputDescriptor);
        OutputStream<KV<String, String>> errorEventsCount = appDescriptor.getOutputStream(outputDescriptor);

        // This very simple operation from https://samza.apache.org/startup/quick-start/latest/samza.html to test this out.
        errorEvents
                .map(kv -> kv.value)
                .window(Windows.keyedSessionWindow(
                w -> w, Duration.ofSeconds(60), () -> 0, (m, prevCount) -> prevCount + 1,
                new StringSerde(), new IntegerSerde()), "count")

                .map(windowPane ->
                        KV.of(windowPane.getKey().getKey(),
                                windowPane.getKey().getPaneId() + ": " + windowPane.getMessage().toString()))
                .sendTo(errorEventsCount);

    }
}
