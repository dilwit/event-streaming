# spring-boot-kafka-stream-processor-nile
Depicts single event stream processing with kafka and spring boot 

### Pre-requisites
- Ref project : `spring-boot-kafka-event-producer-nile` pre-requisites
- Create kafka topics (`enriched-events, error-events`): `kafka-topics --create --topic topic-name --zookeeper localhost:2181 --replication-factor 1 --partitions 1`

### Produce events
- Ref project : `spring-boot-kafka-event-producer-nile` to produce raw-events

### Consume events
- via default consumer provided with kafka
`kafka-console-consumer --topic topic-name --from-beginning --bootstrap-server localhost:9092`