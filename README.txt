This is the manual of how to run the system hosted by containers

Build react image for frontend:
docker build -t react ./Ecommerce-front-end

Build central image for central coordinator:
docker build -t central-coordinator .

Buld replica image for backend server:
docker build -t replica ./Replica

Create docker network that contains all the containers
docker network create appnetwork

Stop all the containers:
docker compose down

Build the docker-compose.yml
docker compose build

Start all the containers/services
docker compose up -d 

This takes a while as there are many resources running. Please wait for 5 mins ...
Check if the container is up by:
docker ps
docker logs <container-name> 

After all the containers starts, initiate database for slave-replicas and master-replica to make data from slave-replicas replicated from master-replica 

chmod +x chmod +x init_replication.sh check_mysql.sh

./init_replication.sh

Test-> go to browser localhost:3000, which hosts the react app


To turn off all services: docker compose down 

To stop a container: docker stop <container_id or name> 

To restart, find the container id or name with: docker ps -a 

Then: docker start <container_id or name> 