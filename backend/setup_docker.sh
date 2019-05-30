#!/bin/bash

# Variables
CONFIG_SERVICE=config-service
CONFIG_IP=172.18.0.8
DISCOVERY_SERVICE=discovery-service
DISCOVERY_IP_PEER1=172.18.0.11
DISCOVERY_IP_PEER2=172.18.0.12 # For one of the clustered discovery services
NETWORK_MASK=172.18.0.22/16
NETWORK_NAME=studentTaskChecker

# Image deletion
docker image rm -f $CONFIG_SERVICE
docker image rm -f $DISCOVERY_SERVICE

# Container deletion
docker container stop $CONFIG_SERVICE
docker container rm $CONFIG_SERVICE
docker container stop $DISCOVERY_SERVICE
docker container rm $DISCOVERY_SERVICE

# Building images from docker files
cd config-service
docker build -f dockerfile -t $CONFIG_SERVICE .
cd ..
cd discovery-service
docker build -f dockerfile -t $DISCOVERY_SERVICE .
cd ..

# Deleting existing network and creating it anew
docker network rm $NETWORK_NAME
docker network create --subnet=$NETWORK_MASK $NETWORK_NAME

# Running docker images
docker run --detach --name $CONFIG_SERVICE --net $NETWORK_NAME --ip $CONFIG_IP -it $CONFIG_SERVICE
docker run --detach --name $DISCOVERY_SERVICE --net $NETWORK_NAME --ip $DISCOVERY_IP_PEER1 -it $DISCOVERY_SERVICE