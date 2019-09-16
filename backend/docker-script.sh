#!/bin/bash
# First argument must be either 'init', 'start', 'stop' or 'recreate + ${containerName}'

# Colors
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
RED='\033[0;31m'
NOCOLOR='\033[0m'

# Conditional variable
cond="$1"
contName="$2"

if [[ "${cond}" == "init" ]]; then
  # Recreates every container, uses no cache, picks up on all updates
  echo -e "\n${YELLOW}Recreating everything from scratch!${NOCOLOR} \n"
  docker-compose build --no-cache && docker-compose up -d --force-recreate && echo -e "\n${GREEN}Everything Docker related is UP and running!${NOCOLOR} \n"
  echo -e "\n${YELLOW}Container name and IP list:${NOCOLOR}"
  docker ps -q | xargs -n 1 docker inspect --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}} {{ .Name }}' | sed 's/ \// /'
  echo && read -n 1 -s -r -p "Press any key to continue..." && echo -e "\n"
else
  if [[ "${cond}" == "stop" ]]; then
    # Stops and deletes all containers and networks, but the docker retains the build components in it's cache
    echo -e "\n${YELLOW}Shutting everything down!${NOCOLOR} \n"
    docker-compose down && echo -e "\n${GREEN}Everything Docker related is DOWN!${NOCOLOR} \n"
  else
    if [[ "${cond}" == "start" ]]; then
      # Starts the containers, it builds from cache, so it doesnt pick up on changes, if a container was already running, this leaves it alone
      echo -e "\n${YELLOW}Starting everything up, building is done from cache, if something was running already, then it's not affected!${NOCOLOR} \n"
      docker-compose up -d && echo -e "\n${GREEN}Everything Docker related is UP!${NOCOLOR} \n"
      echo -e "\n${YELLOW}Container name and IP list:${NOCOLOR}"
      docker ps -q | xargs -n 1 docker inspect --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}} {{ .Name }}' | sed 's/ \// /'
      echo && read -n 1 -s -r -p "Press any key to continue..." && echo -e "\n"
    else
      if [[ "${cond}" == "recreate" ]]; then
        # Rebuilds one container from scratch and starts it
        echo -e "\n${YELLOW}Rebuilding container: ${contName}!${NOCOLOR} \n"
        docker-compose stop "${contName}"
        docker-compose rm -f "${contName}"
        docker-compose build --no-cache "${contName}"
        docker-compose up -d "${contName}" && echo -e "\n${GREEN}${contName} is UP!${NOCOLOR} \n"
        docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}} {{ .Name }}' "${contName}" | sed 's/ \// /'
        echo && read -n 1 -s -r -p "Press any key to continue..." && echo -e "\n"
      else
        echo -e "${RED}Write either 'start' | 'stop' | 'init' | 'recreate' as first argument please!\n${NOCOLOR}"
      fi
    fi
  fi
fi
