# rental

To run the application:

Run infrastructure:
```
 docker-compose -f docker-compose.infrastructure.yml up --build
```

Build the app
```
 ./gradlew build
```

Run app:
```
 docker-compose -f docker-compose.apps.yml up --build
```
Application runs on port 8081 with hostname and context path of ```http://localhost:8081/rental/```

API is documented on Swagger - ```http://localhost:8081/rental/swagger-ui.html```

Registering an user does not require authentication, other api calls require ```Authorization``` header with value of [Base64 representation](https://www.base64encode.org/) of received userId from registration.

  ## Notes
* Spinning up docker env from gradle gives ```invalid tag "939802c0d679cd7bac196634e20e1c3e_rental_-mongo-seed": invalid reference format``` :hmm: