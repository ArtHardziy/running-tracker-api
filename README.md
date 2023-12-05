Running tracker service 

to start app use env vars below:

``-Dspring.profiles.active=dev -Dspring.config.location=classpath:application.yml;file:k8/application-local.yaml``

or use docker:

```shell
docker build -t bermann/running-trakcer .
docker-compose up -d
```