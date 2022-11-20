# rental

To run the application:

Build the app
```
 ./gradlew build
```

Run infrastructure && app:
```
 docker-compose -f docker-compose.infrastructure.yml -f docker-compose.apps.yml up --build
```

Application runs on port 8081 with hostname and context path of ```http://localhost:8081/rental/```

API is documented on Swagger - ```http://localhost:8081/rental/swagger-ui.html```

Registering an user does not require authentication, other api calls require ```Authorization``` header with value of [Base64 representation](https://www.base64encode.org/) of received userId from registration.


Integration tests
```
 ./gradlew integrationTest
```