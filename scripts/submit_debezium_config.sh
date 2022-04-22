#!/usr/bin/env bash

curl -i -X POST -H "Content-Type:application/json" \
  http://localhost:8083/connectors \
  -d '{
        "name": "customerdb-mysql-connector",
        "config": {
            "connector.class": "io.debezium.connector.mysql.MySqlConnector",
            "tasks.max": "1",
            "database.hostname": "mysql",
            "database.port": "3306",
            "database.user": "root",
            "database.password": "mysql",
            "database.allowPublicKeyRetrieval": "true",
            "database.server.id": "184054",
            "database.server.name": "dbserver1",
            "database.whitelist": "customerdb",
            "database.history.kafka.bootstrap.servers": "broker:9092",
            "database.history.kafka.topic": "dbhistory.customerdb",
            "decimal.handling.mode": "double",
            "include.schema.changes": "true",
            "transforms": "unwrap,dropTopicPrefix",
            "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
            "transforms.dropTopicPrefix.type":"org.apache.kafka.connect.transforms.RegexRouter",
            "transforms.dropTopicPrefix.regex":"dbserver1.customerdb.(.*)",
            "transforms.dropTopicPrefix.replacement":"$1",
            "key.converter": "io.confluent.connect.avro.AvroConverter",
            "key.converter.schema.registry.url": "http://schema-registry:8081",
            "value.converter": "io.confluent.connect.avro.AvroConverter",
            "value.converter.schema.registry.url": "http://schema-registry:8081"
        }
     }'


