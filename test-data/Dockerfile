FROM mongo:6.0.3

COPY users.json /users.json
COPY vehicles.json /vehicles.json
COPY reservations.json /reservations.json

CMD mongoimport --host mongodb_container --db rental --collection userDatabaseObject --type json --file /users.json --jsonArray && \
    mongoimport --host mongodb_container --db rental --collection vehicleDatabaseObject --type json --file /vehicles.json --jsonArray && \
    mongoimport --host mongodb_container --db rental --collection reservationDatabaseObject --type json --file /reservations.json --jsonArray
