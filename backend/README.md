# How to run backend

You must use Linux (some Ubuntu is preferred) for now, and have docker installed (please configure so it doesnt need sudo) <br>
and cd into this directory. After that run 'mvn clean install' just run 'bash setup_docker.sh'. <br>
It may take some time (and luck :D) and you can view the peer1 eureka instance web-page on 172.18.0.11/8761. <br>
More services will be getting docker files after they are more or less done. 

# When a service is in the making just start those docker images which are necessary (config and discovery service plus any dependent service) and you can test the service on localhost launched from the IDE