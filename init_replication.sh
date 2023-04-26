#!/bin/bash

# Wait for master to start
sleep 30

# Configure replication on each slave
for i in {1..3}; do
  # Get the master's binary log coordinates
  MASTER_STATUS=$(docker exec -it mysqldb mysql -uroot -proot -e "SHOW MASTER STATUS\G")
  FILE=$(echo "$MASTER_STATUS" | grep File | awk '{print $2}')
  POSITION=$(echo "$MASTER_STATUS" | grep Position | awk '{print $2}')

  # Set up the slave with the master's binary log coordinates
  docker exec -it mysqldb$i mysql -uroot -proot -e "CHANGE MASTER TO MASTER_HOST='mysqldb', MASTER_USER='root', MASTER_PASSWORD='root', MASTER_LOG_FILE='$FILE', MASTER_LOG_POS=$POSITION; START SLAVE;"
done
