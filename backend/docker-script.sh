#!/bin/bash
# First argument must be either 'start' or 'stop'

# Colors
YELLOW='\033[1;33m'
GREEN='\033[0;32m'
RED='\033[0;31m'
NOCOLOR='\033[0m'

# Conditional variable
cond="$1"

if [[ "${cond}" == "start" ]]; then
  echo -e "\n${YELLOW}Running docker-compose build and init!${NOCOLOR} \n"

  # In development use - this line always rebuilds the images and containers
  docker-compose build --no-cache && docker-compose up -d --force-recreate && echo -e "\n${GREEN}Everything Docker related is UP and running!${NOCOLOR} \n"

  # In production use - it uses the cache to speed up image creation, and does not pick up on changes
  #docker-compose up --build --force-recreate -d && echo -e "\n${GREEN}Everything Docker related is UP and running!${NOCOLOR} \n"

  echo -e "\n${YELLOW}Container name and IP list:${NOCOLOR}"
  docker ps -q | xargs -n 1 docker inspect --format '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}} {{ .Name }}' | sed 's/ \// /'

else
  if [[ "${cond}" == "stop" ]]; then
    echo -e "\n${YELLOW}Shutting down everything!${NOCOLOR} \n"
    docker-compose down && echo -e "\n${GREEN}Everything Docker related is DOWN!${NOCOLOR} \n"
  else
    echo -e "${RED}Write either 'start' or 'stop' as first argument please!\n"
  fi
fi
