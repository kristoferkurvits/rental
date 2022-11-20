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
Swagger - ```http://localhost:8081/rental/swagger-ui.html```

Registering an userRegistrationRequest does not require authentication, other api calls require ```Authorization``` header with value of [Base64 representation](https://www.base64encode.org/) of received userId from registration.

## Example payloads (Use these instead of clicking on the "Model Schema" on swagger)
POST /register
Register a new userRegistrationRequest
```
{
    "email": "email1@gmail.com",
    "password": "dontreadme",
    "name": "name"
}
```

POST /createVehicleRequest
Create createVehicleRequest
```
{
    "state_of_charge": 100,
    "coordinates": {
		"latitude": 59.436962
        "longitude": 24.753574,
    }
}
```

PUT /createVehicleRequest
Update createVehicleRequest
```
{
    "state_of_charge": 100,
    "coordinates": {
        "longitude": 10.0,
        "latitude": 10.5
    }
}
```

POST /rent/start
Start renting a createVehicleRequest
```
{
    "user_id": {id received from registration},
    "vehicle_id": {id received from creating a createVehicleRequest}
}
```

POST /rent/end
End renting a createVehicleRequest
```
{
    "user_id": {id received from registration},
    "vehicle_id": {id received from creating a createVehicleRequest}
}
```


  ## Notes
* Spinning up docker env from gradle gives ```invalid tag "939802c0d679cd7bac196634e20e1c3e_rental_-mongo-seed": invalid reference format``` :hmm: