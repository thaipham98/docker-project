#!/bin/bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 <number_of_replicas>"
  exit 1
fi

num_replicas=$1

for ((i=1; i<=num_replicas; i++)); do
  cat > "src/main/resources/application-replica${i}.properties" <<- EOM
server.port=100${i}

# Application's database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/store${i}?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=gus101997
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
EOM

  echo "Starting replica ${i} on port 100${i}..."
  mvn spring-boot:run -Dspring-boot.run.profiles=replica${i} &
  sleep 5
done
#
#server.port=1001
#
## Application's database configuration
#spring.datasource.url=jdbc:mysql://localhost:3306/store1?createDatabaseIfNotExist=true
#spring.datasource.username=root
#spring.datasource.password=gus101997
#spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
#spring.jpa.hibernate.ddl-auto=none
#spring.sql.init.mode=always
