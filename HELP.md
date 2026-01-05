# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.ciberaccion.chisme-chat' is invalid and this project uses 'com.ciberaccion.chisme_chat' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.5.8/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.5.8/maven-plugin/build-image.html)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/3.5.8/reference/testing/testcontainers.html#testing.testcontainers)
* [Spring Web](https://docs.spring.io/spring-boot/3.5.8/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.8/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/3.5.8/reference/web/spring-security.html)
* [Testcontainers](https://java.testcontainers.org/)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)

### Testcontainers support

This project uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.5.8/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:


Please review the tags of the used images and set them to the same as you're running in production.

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

## How to use

Run local in dev  
start application:
- In VSC in spring boot dashoard, in APPS section press run button
- in command line: $ mvn spring-boot:run

Optional, H2 console http://localhost:8080/h2-console/login.jsp  

OJO aqui: si quiere correr en dev tiene que descomentar la linea spring.profiles.active=dev  
en application.properties para que use application-dev.properties  

Run local in Dockers  
ejecutar docker-compose para crear las imagenes backend-api y mysql-db y correr los containers  
$ docker-compose up  

Si tu docker-compose.yml todavía tiene build:, puedes evitar la reconstrucción usando:  

$ docker-compose up --no-build  
o puedes correr desde docker desktop, corre el "compose stack" chisme-chat  



### Run Performance Test

Performance Test script: chisme-chat/performance/smoke-e2e.js  
see smoke test section below

pre-requisite: run cleanup.sql

### Test local with H2

Start local application  
Postman Collection chismechat  
located in project root in docs folder: chisme-chat/docs/chismechat.postman_collection

### Test local with Docker Containers

Start local containers  
Postman Collection chismechat  
located in project root in docs folder: chisme-chat/docs/chismechat.postman_collection

Optional: use Mysql workbench  
datasource.url=jdbc:mysql://mysql:3306/mydb  
user: user pass: password  

SMOKE TEST should be run local in docker  
location: chisme-chat/performance/smoke-e2e.js  
how to run: $ ./run-smoke.sh
the script will first clean the database using cleanup.sql and then execute the tests  
the script uses chocolately to run in windows, modify runnign env if need it

### Test in cloud AWS

La url puede variar ya que cada se corre el environment de ElasticBeanstalk se crea  
una url diferente.  
verificar que la url esta activa con el endpoint health  
verificar/actualizar la url en el environment del postman collection 

Postman Collection chismechat  
located in project root in docs folder: chisme-chat/docs/chismechat.postman_collection