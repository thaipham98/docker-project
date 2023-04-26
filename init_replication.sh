#!/bin/bash

sleep 30

# Create a dedicated replication user on the master
docker exec -it docker-project-mysqldb-1 mysql -uroot -proot -e "CREATE USER 'replication'@'%' IDENTIFIED WITH mysql_native_password BY 'password';"
docker exec -it docker-project-mysqldb-1 mysql -uroot -proot -e "GRANT REPLICATION SLAVE ON *.* TO 'replication'@'%';"
docker exec -it docker-project-mysqldb-1 mysql -uroot -proot -e "FLUSH PRIVILEGES;"

for i in {1..3}; do
  MASTER_STATUS=$(docker exec -it docker-project-mysqldb-1 mysql -uroot -proot -e "SHOW MASTER STATUS\G")
  FILE=$(echo "$MASTER_STATUS" | grep "File" | awk '{print $2}')
  POSITION=$(echo "$MASTER_STATUS" | grep "Position" | awk '{print $2}')

  docker exec -it docker-project-mysqldb${i}-1 mysql -uroot -proot -e "STOP SLAVE;"
  docker exec -it docker-project-mysqldb${i}-1 mysql -uroot -proot -e "CHANGE MASTER TO MASTER_HOST='docker-project-mysqldb-1', MASTER_PORT=3306, MASTER_USER='replication', MASTER_PASSWORD='password';"
  docker exec -it docker-project-mysqldb${i}-1 mysql -uroot -proot -e "START SLAVE;"
done