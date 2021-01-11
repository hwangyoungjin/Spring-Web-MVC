## Springboot-data
---
1. 스프링 데이터 1부
	```
	* 프로젝트 : 프로젝트 spring initialize -> web, jdbc, h2 의존성 추가 //자동으로 버전관리된다.
	*목차
	1. SQL DB
	- 인메모리 데이터 베이스
	- databaseSource 설정
	- DBCP
	- JDBC
	- 스프링 데이터 JPA
	- JOOQ 사용
	- 데이터베이스 초기화
	- 데이터베이스 마이그레이션 툴 연동
	
	2. NoSQL
	- Redis(key/value)
	- MongoDB(Document)
	- Neo4J(Graph)
	```
2. 스프링 데이터 2부 : 인메모리 데이터 베이스 (h2, jdbc)
	1. **인메모리 데이터베이스 : 데이터 스토리지의 메인 메모리에 설치되어 운영되는 방식의 데이터베이스 관리 시스템**
	2. **H2DB : 자바 기반의 오픈소스 관계형 데이터 베이스 관리 시스템(RDBMS)**
	```java
	* jdbc의존성추가로 Spring-JDBC가 classpath에 있기때문에 
	스프링부트 autoconfiguration의 자동설정이 필요한 빈을 설정해준다.
	
	* h2 인메모리 데이터베이스 사용하는 jdbc설정이 되어 application 동작
	@Component
	public class H2Runner implements ApplicationRunner {
	
	    @Autowired
	    DataSource dataSource;
	
	    @Override
	    public void run(ApplicationArguments args) throws Exception {
	        try(Connection connection = dataSource.getConnection()){
	            System.out.println(connection.getMetaData().getURL());
	            System.out.println(connection.getMetaData().getUserName());
	            Statement statement = connection.createStatement();
	            String sql = "CREATE TABLE USER(" +
	                    "ID INTEGER NOT NULL, " +
	                    "name VARCHAR(255), " +
	                    "PRIMARY KEY (id))";
	            statement.executeUpdate(sql);
	        }
	    }
	}
	```
3. 스프링 데이터 3부 : MySQL	
	- DBCP는 DataBase Connection Pool 의 약자로 DB와 커넥션을 맺고 있는 객체를 관리하는 역할
	- DBCP는 DB 연결 시 마다 (JDBC만 있는경우 수행되는)Driver를 로드하고 커넥션 객체를 얻는 작업을 반복
	- hikariCP는 스프링 부트 2.0부터 default JDBC connection pool이다.
	- 도커는 애플리케이션 실행에 필요한 환경을 하나의 이미지로 모아두고 그 이미지를 사용해 다양한 환경에서 실행 환경을 구축, 운용하기 위한 오픈소스 플랫폼
4. 스프링 데이터 4부 : postgrsSQL [**Pass**]
5. 스프링 데이터 5부 6부 : Spring boot data JPA 연동
	- JPA 의존성 추가
	```java
	<dependency>
	      <groupId>org.springframework.boot</groupId>
	      <artifactId>spring-boot-starter-data-jpa</artifactId>
	</dependency>
	```
	- jpa 상속받은 Repository만들기
	- h2 인메모리 사용하여 jpa 사용하기
6. 스프링 데이터 7부~12부 정리 : 도커 학습 후 다시 공부