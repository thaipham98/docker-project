#!/bin/bash

container_name="$1"
sql_command="$2"

until docker exec -it "$container_name" mysql -uroot -proot -e "SELECT 1" > /dev/null 2>&1; do
  echo "Waiting for MySQL container ($container_name) to be ready..."
  sleep 5
done

docker exec -it "$container_name" mysql -uroot -proot -e "$sql_command"