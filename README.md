[![Build Status](https://travis-ci.org/springuni/springuni-particles.svg?branch=master)](https://travis-ci.org/springuni/springuni-particles)
[![License: LGPL v3](https://img.shields.io/badge/License-LGPL%20v3-blue.svg)](https://www.gnu.org/licenses/lgpl-3.0)  

# SpringUni particles

`springuni-particles` is a toolbox for creating small footprint user management microservices.
This repository is the first round implementation of that system which was described with article series
[A deep dive into implementing a user management application with Spring Boot](https://springuni.com/user-management-microservice/).

## Installation

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

## Usage

TODO: Write usage instructions

## Contributing

1. Fork it!
2. Create an [issue](https://github.com/springuni/springuni-particles/issues) describing the feature or bug fix you'd like to submit
3. Create your feature branch: `git checkout -b my-new-feature`
4. Commit your changes: `git commit -am '#N Add some feature'`

_Note: Use the issue number `#N` as the first word of every commit message._

5. Push to the branch: `git push origin my-new-feature`
6. Submit a pull request :D

## History

TODO: Write history

## Credits

TODO: Write credits

## License

This library, *springuni-particles*, is free software ("Licensed
Software"); you can redistribute it and/or modify it under the terms of the [GNU
Lesser General Public License](https://www.gnu.org/licenses/lgpl-3.0.en.html) as
published by the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

This library is distributed in the hope that it will be useful, but WITHOUT ANY
WARRANTY; including but not limited to, the implied warranty of MERCHANTABILITY,
NONINFRINGEMENT, or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
Public License for more details.

You should have received a copy of the [GNU Lesser General Public
License](https://www.gnu.org/licenses/lgpl-3.0.en.html) along with this library; if
not, see [GNU Licenses](http://www.gnu.org/licenses/).
