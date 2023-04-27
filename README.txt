Build react image: docker build -t react ./Ecommerce-front-end
Build central image: docker build -t central-coordinator .
Build replica image: docker build -t replica ./Replica

Create docker network: docker network create appnetwork

docker compose down
docker compose build
docker compose up -d 

Wait for 5 mins ... 

Replicate database:

chmod +x chmod +x init_replication.sh check_mysql.sh
./init_replication.sh

Test-> go to browser localhost:3000

To turn off all services: docker compose down 