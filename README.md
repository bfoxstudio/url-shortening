#### For testing purposes, I prepared a service without authentication and caching  
Standalone URL Shortening Service  
```shell
docker run -d -p 8080:8080 -t --rm wimbd/url-shortening-service:latest
```

#### For testing, you can use the following requests  
When launching the `url-shortening-service`, which has integration with Keycloak, you don't need to specify a `userId`.

###### Create a short link
```shell
curl -X POST http://localhost:8080/api/v1/url/create-short \
--header 'Content-Type: application/json' \
--data '{
    "url": "https://example.com",
    "userId": "550e8400-e29b-41d4-a716-446655440000"
}'
```

###### Follow the link
```shell
curl -v http://localhost:8080/62cebf0
```

###### Get all links for the user
```shell
curl http://localhost:8080/api/v1/user/550e8400-e29b-41d4-a716-446655440000/urls
```

###### Disable the link
```shell
curl -X DELETE http://localhost:8080/api/v1/user/550e8400-e29b-41d4-a716-446655440000/urls/62cebf0
```

#### To use all services, you need to run Keycloak and Redis containers
Keycloak
```shell
docker run -p 8181:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:21.1.1 start-dev
```

Redis
```shell
docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
```

Then configure Keycloak and update the `application.yaml` file (`url-shortening-service/src/main/resources/application.yaml:25`). After that, you can start the `discovery-server`, `api-gateway`, and `url-shortening-service`.

#### The project includes an integration test for `url-shortening-service`
```
url-shortening-service/src/test/java/com/example/urlshorteningservice/UrlShorteningServiceApplicationTests.java
```