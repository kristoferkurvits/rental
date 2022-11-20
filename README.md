# rental

Run local infrastructure:
```
 docker-compose -f docker-compose.infrastructure.yml up --build
```

Registering an user does not require authentication, other api calls require ```Authorization``` header with value of Base64 representation of received userId from registration.

  ## Notes
* Spinning up docker env from gradle gives ```invalid tag "939802c0d679cd7bac196634e20e1c3e_rental_-mongo-seed": invalid reference format``` :hmm: