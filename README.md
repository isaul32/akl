Akateeminen Kynäriliiga (AKL)
=============================

# Install
```
npm install -g gulp
npm install -g typings
npm install
typings install
mvn install
cp .akl.properties ~/
```
# Run backend development
```
cd akl-service/
mvn spring-boot:run
```
# Run frontend development
```
cd akl-app/
gulp serve
```

# Configs
## Backend 
Put all backend private configs to ~/.akl.properties file. 

## Frontend
Frontend configs are at gulpfile.js file.

# Tools
Maven 3, Node, NPM, Gulp...

# Technologies

## Backend
Java 8, Postgres, Tomcat 8 (WebSocket), Spring, H2, Ehcache, Swagger, Thymeleaf...

## Frontend
AngularJS, AngularUI Router, Restangular, Typescript, Bootstrap...
