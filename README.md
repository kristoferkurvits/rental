# rental

Run local infrastructure:
```
 docker-compose -f docker-compose.infrastructure.yml up --build
```

Registering an user does not require authentication, other api calls require ```Authorization``` header with value of Base64 representation of received userId from registration.

  