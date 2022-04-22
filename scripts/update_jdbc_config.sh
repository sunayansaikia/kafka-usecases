#!/usr/bin/env bash

curl -i -X PUT -H "Content-Type:application/json" \
  http://localhost:8083/connectors/customerdb-jdbc-connector/config \
  -d '{
          "connector.class": "io.confluent.connect.jdbc.JdbcSourceConnector",
          "connection.url": "jdbc:mysql://mysql:3306/customerdb?allowPublicKeyRetrieval=true&useSSL=false",
          "connection.user": "root",
          "connection.password": "mysql",
          "topic.prefix": "mysql-20-",
          "mode":"incrementing",
          "table.whitelist" : "customerdb.customer",
          "incrementing.column.name": "id",
          "validate.non.null": false    
     }'


