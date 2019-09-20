### How to run backend
Linux or MacOS is preferred, Windows works too but Docker shows unpredictable behaviour there sometimes.
Install Docker and Docker-compose and configure it, so it doesn't need sudo (mainly for convenience's sake).
After a fresh pull or if you change something, you have to 'mvn clean install' the respective modules.
To run the services use docker-script.sh. <br>
It accepts 4 arguments: <br>
    - init: forcibly recreates containers, picks up on changes, slow <br>
    - start: starts every container, if something needs building it uses cached resources so doesn't pick up on changes,
             doesn't affect running containers, fast <br>
    - stop: stops and removes all containers, removes the network <br>
    - recreate ${container_name}: recreates a container completely from scratch <br>
    - restart ${container_name}: restarts a container <br>

### Other infos
Services reach each other by their container names, thanks to the Docker built-in DNS service.
Their IP is dynamically set, the script shows them but you can 'docker inspect ${container_name}' to see them for yourself.
From browser, you can reach the containers by their IPs and Ports.
To read logs use Kitematic or 'docker logs ${container_name} -f' command.
To check out a container's file system use 'docker exec -it ${container_name} bash' command.

### Windows solutions
To reach containers you must run 'route add 172.18.0.0 mask 255.255.0.0 10.0.75.2 -p'.