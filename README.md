# 

## Local development

1) Create `.env` with the following contents

```
PORT=5000
...
```

2a) Compile and build with

    `mvn clean install`
    
2b) Compile and build with skipping the integration tests

    `mvn clean install -Dskip.integration.tests=true`

3) Start locally with

    `mvn -pl ... spring-boot:run`

## Deployment to ...

