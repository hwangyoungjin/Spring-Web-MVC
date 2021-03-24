# Spring-boot-Media-Api
---

1. 환경
```java
* jdk 11
* Gradle
* SpringBoot 2.4.3
* Dependency
 - Web
 - Lombok
 - Spring Boot Jpa
 - Spring Security
 - Devtools
 - H2DB
```
2. application.properties 설정
```properties
#H2DB
spring.jpa.hibernate.ddl-auto=create
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.type.descriptor.sql=trace
```