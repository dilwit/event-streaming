# apache-samza-stream-processor-nile
Depicts multiple (stateful) event stream processing with kafka and apache samza

### Pre-requisites
- Ref project : `spring-boot-kafka-stream-processor-nile` pre-requisites
- Create kafka topics (`one-minute-error-events-counter): `kafka-topics --create --topic topic-name --zookeeper localhost:2181 --replication-factor 1 --partitions 1`
- Run (with the program args): --config-path /Users/dilusha.withanage/Documents/GitHub/apache-samza-stream-processor-nile/src/main/resources/error-event.properties"

### Produce events
- Ref project : `spring-boot-kafka-stream-processor-nile` to produce raw-events

### Consume events
- via default consumer provided with kafka
`kafka-console-consumer --topic topic-name --from-beginning --bootstrap-server localhost:9092`
