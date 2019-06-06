#!/bin/bash

# Colors
YELLOW='\033[1;33m'
NOCOLOR='\033[0m'

# Variables
CONFIG_SERVICE="config-service"
DISCOVERY_SERVICE="discovery-service"

NETWORK_MASK="172.18.0.22/16"
NETWORK_NAME="studentTaskChecker"

#CONFIG_IP="172.18.0.8"
#DISCOVERY_IP_PEER1="172.18.0.11"
#DISCOVERY_IP_PEER2="172.18.0.12" # For one of the clustered discovery services

echo -e "\n${YELLOW}Image deletion${NOCOLOR} \n"

docker image rm -f ${CONFIG_SERVICE}
docker image rm -f "${DISCOVERY_SERVICE}-peer1"
docker image rm -f "${DISCOVERY_SERVICE}-peer2"

echo -e "\n${YELLOW}Container deletion${NOCOLOR} \n"

docker container stop ${CONFIG_SERVICE}
docker container rm ${CONFIG_SERVICE}
docker container stop "${DISCOVERY_SERVICE}-peer1"
docker container rm "${DISCOVERY_SERVICE}-peer1"
docker container stop "${DISCOVERY_SERVICE}-peer2"
docker container rm "${DISCOVERY_SERVICE}-peer2"

echo -e "\n${YELLOW}Building images from docker files${NOCOLOR} \n"

cd config-service
docker build -f dockerfile -t ${CONFIG_SERVICE} .
cd ..
cd discovery-service
docker build -f dockerfile-p1 -t "${DISCOVERY_SERVICE}-peer1" .
docker build -f dockerfile-p2 -t "${DISCOVERY_SERVICE}-peer2" .
cd ..

echo -e "\n${YELLOW}Deleting existing network and creating it anew${NOCOLOR} \n"

docker network rm ${NETWORK_NAME}
docker network create --subnet=${NETWORK_MASK} ${NETWORK_NAME}

echo -e "\n${YELLOW}Running docker images${NOCOLOR} \n"

docker run --detach --name ${CONFIG_SERVICE} --net ${NETWORK_NAME} -it ${CONFIG_SERVICE}
docker run --detach --name "${DISCOVERY_SERVICE}-peer1" --net ${NETWORK_NAME} -it "${DISCOVERY_SERVICE}-peer1"
docker run --detach --name "${DISCOVERY_SERVICE}-peer2" --net ${NETWORK_NAME} -it "${DISCOVERY_SERVICE}-peer2"