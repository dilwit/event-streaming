# spring-boot-kafka-event-producer-nile

### Pre-requisites
- Install: `brew install kafka`
- Run: `zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties`
- Note: Zookeeper Used for cluster coordination
- Create kafka topics (`raw-events`): `kafka-topics --create --topic topic-name --zookeeper localhost:2181 --replication-factor 1 --partitions 1`
- List available topics: `kafka-topics --list --zookeeper localhost:2181`

### Produce raw-events
- via default producer provided with kafka
`kafka-console-producer --topic raw-events --broker-list localhost:9092`

- via the rest end-point provided in this application
`curl -d '{"event":"SHOPPER_VIEWED_PRODUCT","shopper":{"id":"123","name":"Jane","ipAddress":"70.46.123.145"},"product":{"sku":"aapl-001","name":"iPad"},"timestamp":"2018-10-15T12:01:55Z"}' -H "Content-Type: application/json" -X POST http://localhost:9000/events`

### Consume raw-events
- via default consumer provided with kafka
`kafka-console-consumer --topic raw-events --from-beginning --bootstrap-server localhost:9092`

### RawEvent Payload
`{
   "event": "SHOPPER_VIEWED_PRODUCT",
   "shopper": {
     "id": "123",
     "name": "Jane",
     "ipAddress": "70.46.123.145"
   },
   "product": {
     "sku": "aapl-001",
     "name": "iPad"
   },
   "timestamp": "2018-10-15T12:01:35Z"
 }`