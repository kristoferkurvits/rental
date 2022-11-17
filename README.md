# rental

Run local infrastructure:
```
 docker-compose -f docker-compose.infrastructure.yml up --build
```

## Notes
* ~~Having problems with Lombok annotation processing during compilaton.~~
* I might be overthinking but the userID is generated after the registration which means I can't check if the user exists before registration.
  I could check by email but setting email as ID in DB doesn't seem to be a good idea and the problems will carry on to authentication. Querying NoSQL isn't nice as well.
  I'm thinking about generating backwards compatible ID from email. Rethink data model. Morning is wiser than evening.
  