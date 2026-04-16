#!/bin/bash
set -e

# Генерируем основной конфиг сервера
cat > /tmp/server.properties <<EOF
process.roles=broker,controller
node.id=1
controller.quorum.voters=1@kafka:9093

listeners=INTERNAL://0.0.0.0:9092,CONTROLLER://0.0.0.0:9093,EXTERNAL://0.0.0.0:9094
advertised.listeners=INTERNAL://${KAFKA_INTERNAL_URL},EXTERNAL://localhost:9094
listener.security.protocol.map=INTERNAL:SASL_PLAINTEXT,CONTROLLER:SASL_PLAINTEXT,EXTERNAL:SASL_PLAINTEXT
inter.broker.listener.name=INTERNAL
controller.listener.names=CONTROLLER

sasl.enabled.mechanisms=SCRAM-SHA-512
sasl.mechanism.inter.broker.protocol=SCRAM-SHA-512
sasl.mechanism.controller.protocol=SCRAM-SHA-512

listener.name.internal.scram-sha-512.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="${BROKER_KAFKA_USERNAME}" password="${BROKER_KAFKA_PASSWORD}";
listener.name.controller.scram-sha-512.sasl.jaas.config=org.apache.kafka.common.security.scram.ScramLoginModule required username="${BROKER_KAFKA_USERNAME}" password="${BROKER_KAFKA_PASSWORD}";

authorizer.class.name=org.apache.kafka.metadata.authorizer.StandardAuthorizer
allow.everyone.if.no.acl.found=false
super.users=User:admin;User:broker

auto.create.topics.enable=false
delete.topic.enable=true

num.partitions=3
offsets.topic.replication.factor=1
transaction.state.log.replication.factor=1
transaction.state.log.min.isr=1
group.initial.rebalance.delay.ms=0
log.dirs=/var/lib/kafka/data
EOF

# Генерируем JAAS конфиг для запуска сервера
cat > /tmp/kafka_server_jaas.conf <<EOF
KafkaServer {
  org.apache.kafka.common.security.scram.ScramLoginModule required
  username="${BROKER_KAFKA_USERNAME}"
  password="${BROKER_KAFKA_PASSWORD}";
};
EOF

export KAFKA_OPTS="-Djava.security.auth.login.config=/tmp/kafka_server_jaas.conf"

# Форматируем хранилище, если оно пустое
if [ ! -f /var/lib/kafka/data/meta.properties ]; then
  /opt/kafka/bin/kafka-storage.sh format \
    --ignore-formatted \
    --cluster-id MkU3OEVBNTcwNTJENDM2Qk \
    --add-scram "SCRAM-SHA-512=[name=${ADMIN_KAFKA_USERNAME},password=${ADMIN_KAFKA_PASSWORD}]" \
    --add-scram "SCRAM-SHA-512=[name=${BROKER_KAFKA_USERNAME},password=${BROKER_KAFKA_PASSWORD}]" \
    --add-scram "SCRAM-SHA-512=[name=${FF_SERVICE_KAFKA_USERNAME},password=${FF_SERVICE_KAFKA_PASSWORD}]" \
    -c /tmp/server.properties
fi

# Запускаем Kafka
exec /opt/kafka/bin/kafka-server-start.sh /tmp/server.properties