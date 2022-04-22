#!/usr/bin/env bash

curl -i -X PUT -H "Content-Type:application/json" \
  http://localhost:8083/connectors/customerdb-mysql-connector2/config \
  -d '{
        "connector.class": "io.debezium.connector.mysql.MySqlConnector",
        "tasks.max": "1",
        "database.hostname": "mysql",
        "database.port": "3306",
        "database.user": "root",
        "database.password": "mysql",
        "database.allowPublicKeyRetrieval": "true",
        "database.server.id": "102",
        "database.server.name": "dbserver102",
        "database.whitelist": "customerdb.customer",
        "database.history.kafka.bootstrap.servers": "broker:29092",
        "database.history.kafka.topic": "customerdb.history102",
        "decimal.handling.mode": "double",
        "include.schema.changes": "true",
        "transforms.unwrap.type": "io.debezium.transforms.ExtractNewRecordState",
        "key.converter": "io.confluent.connect.avro.AvroConverter",
        "key.converter.schema.registry.url": "http://schema-registry:8081",
        "value.converter": "io.confluent.connect.avro.AvroConverter",
        "value.converter.schema.registry.url": "http://schema-registry:8081"
     }'


