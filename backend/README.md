# How to run backend

You must use Linux (some Ubuntu is preferred) for now, and have docker installed (please configure so it doesnt need sudo) <br>
and cd into the 'backend' directory. After that run 'mvn clean install' and just run 'bash setup_docker.sh'. <br>
The file deletes previous versions of docker artifacts, creates them again and sets them up. <br>
Windows docker had a strange behaviour when I tried so it's not advised as of yet. <br>
It may take some time (and luck :D) and you can view the 'peer1' eureka instance web-page on '172.18.0.11/8761'. There you can check if everything is in order <br>
More services will be getting Docker files after they are more or less done. Until then you run in-prod services from intellij in conjunction with the ones running on docker. <br>
If you dont want to use Docker there is a way, it involves some property changes but that is it. <br>

# When a service is in the making just start those docker images which are necessary (config and discovery service plus any dependent service) and you can test the service on localhost launched from the IDE