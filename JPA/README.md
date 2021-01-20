# JPA
---

## 순서
---
1. JPA 핵심개념
2. 스프링 데이터 JPA 활용 
---
1. ### JPA 핵심개념이해 
	1. #### 프로젝트 셋팅
	- 환경
	```java
	* spring initializer
	* jdk 1.8
	* Maven
	* docker - postgreSQL
	```
		1. ##### postgres Docker에 설치
			```java
			* 순서
			1. docker run -p 5432:5432 -e POSTGRES_PASSWORD=pass -e POSTGRES_USER=young -e POSTGRES_DB=springdata --name postgres_boot -d postgres
			2. docker exec -i -t postgres_boot bash
			3. su - postgres
			4. psql springdata
			--> 여기서 오류발생[윈도우에서 하는경우 발생함]
			--> 이유 : 기본으로 명시한 디비에 접근할 때 postgres라는 유저로 접근을 시도하기 떄문
			--> 해결방법 : psql --username young --dbname springdata 로 해결
			5. 이후 sql 문법 사용가능
			---> create table account (id int, username varchar(255), password varchar(255));
			---> insert into account values(1, 'y', 'pass');
			---> select * from account;
			```
		2. #### SpringDataJPA 의존성추가
			```java
			* JPA v2.*
				* Hibernate v5.*
			* HibernateJpaAutoConfiguration 자동설정 된다.
			```
		3. ####  Entity로 db 저장 - EntityManager사용
			1. account Model 만들기
			2. postgres 드라이버를 위해 의존성추가
			3. Runner과 EntityManager  통해 db저장
			```java
			@Component
			@Transactional//모든 operation은 하나의 트랜잭션 안에서 일어나야 하므로 해당 기능 사용
			//class, method 레벨에서 사용 가능
			public class JpaRunner implements ApplicationRunner {
			
			    @PersistenceContext
			    EntityManager entityManager; //jpa의 핵심 (스프링의 핵심인 applicationContext 처럼)
			    //해당 클래스를 통해 db 영속화(저장) 가능
			
			    @Override
			    public void run(ApplicationArguments args) throws Exception {
			        Account account = new Account();
			        account.setUsername("young"); 
			        account.setPassword("pass");
			        entityManager.persist(account);
			    }
			}
			```

 	2. #### 엔티티 타입 맵핑
	
	
	