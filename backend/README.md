# How to run backend

You must use Linux (some Ubuntu is preferred) for now, and have Docker and Docker-Compose installed (please configure so they doesnt need sudo)
and **cd into the 'backend' directory**. After that **run 'mvn clean install' and just run 'bash docker_script.sh $arg'**, in which ** *$arg is either 'start' or 'stop'* **.
Start creates a network, images of the services, and containers from the images on said network. Services can reach each other by container name, so IP is
dynamically set. Docker-compose should always rebuilds images, so if there are changes in the source code be sure to run 'mvn clean install'.
Windows docker had a strange behaviour when I tried so it's not advised as of yet. When the containers are running you can 'docker inspect container_name',
and search for its IP address, so you can reach it from a browser. It may take some time (and luck :D) and you can use 'docker inspect backend_studentTaskChecker'
command to see the container IP's on the network. Based on the IPs you can reach the containers from a browser. (${IP}:${PORT})
More services will be getting Docker files after they are more or less done. Until then you run in-dev services from intellij in conjunction with the ones
running on docker. If you dont want to use Docker there is a way, it involves some property changes but that is it.
**The easiest way to read logs is the 'docker logs ${service-name} -f' command.**
Use 'docker exec -it name of container bash' to check filesystem thingies in a container. Volumes are hard to figure out y'know.
If you want to read logs while in a container use 'cat' or install nano if you want.

# Windows solutions
In Windows there are heavy docker problems. To reach containers you must run 'route add 172.18.0.0 mask 255.255.0.0 10.0.75.2 -p', but that can cause internet outage somehow.