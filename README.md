Akateeminen Kynäriliiga
==========================

# Install
```
npm install -g gulp
mvn install
cp .akl.properties ~/
```
# Run backend development
```
cd service/
mvn spring-boot:run
```
# Run frontend development
```
cd app/
gulp serve
```

# Private configs
Put all private configs to ~/.akl.properties file. 

# Tools
Maven 3, Node, NPM, Gulp...

# Technologies

## Backend
Java 8, Postgres, Tomcat 8 (WebSocket), Spring, H2, Ehcache, Swagger, Thymeleaf...

## Frontend
AngularJS, AngularUI Router, Bootstrap...
