
[![Build Status](https://travis-ci.org/springuni/springuni-particles.svg?branch=master)](https://travis-ci.org/springuni/springuni-particles)
# 

## Local development

1) Create `.env` with the following contents

```
PORT=5000
JDBC_DATABASE_URL=jdbc:mysql://<user>:<password>@localhost:3306/springuni?serverTimezone=UTC
JWT_TOKEN_SECRET_KEY=<strong random alpha numeric sequence>
REMEMBER_ME_TOKEN_SECRET_KEY=<strong random alpha numeric sequence>
...
```

2a) Compile and build with

    `./mvnw clean install`
    
2b) Compile and build with skipping the integration tests

    `./mvnw clean install -Dskip.integration.tests=true`

3) Start locally with

    `./mvnw -pl springuni-auth-boot spring-boot:run`

## Deployment to ...
