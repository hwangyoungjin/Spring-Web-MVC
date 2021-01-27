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
		```java
		 - @Entity
		    1. JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 어노테이션을 필수
		    2. @Entity가 붙은 클래스는 JPA가 관리
		    3. 엔티티라 부른다.
		 - @Table
		    1. 엔티티와 매핑할 테이블을 지정합니다.
		    2. 생략하면 매핑한 엔티티 이름(@Entity가 붙은 class명)을 테이블 이름으로 사용합니다.
		 - @Id
		    1. 엔티티의 주키를 맵핑할때 사용
		 - @GeneratedValue
		    1. 주키의 생성 방법을 맵핑
		    2. 생성 전략과 생성기 설정가능 (4가지)
                                 : 기본 전략을 AUTO로 null들어가면 자동증가
		 - @Column
		    1. 해당 애노테이션으로 필드 속성 값 지정가능
		    2. class level에 @Entity있는 경우 생략가능	
		 - @Temporal
		    1. java.util.Data 와 java.util.Calender만 지원
		 - @Transient
		    1. column으로 사용하고 싶지 않은 경우 사용
		 - JPA 로거 (아래 2개를 세트로 많이  사용)
		    - spring.jap.show-sql=true // 실행되는 sql 확인가능 
		    - spring.jpa.properties.hibernate.format_sql=true // 실행되는 sql 깔끔하게
		```
 	3. #### Value 타입 맵핑
		- 엔티티 타입과 Value 타입 분류
		- Value 타입에는 3가지
			1. 기본타입
			2. Composite Value 타입
			3. Collection Value 타입
		- Composite Value 타입
			1. @Embeddable
			2. @Embedd
			3. @AttributeOverrides
			4. @AttributeOverride
			```java
			@Entity
			public class Account {

			    @Id @GeneratedValue
			    private Long id;
			    @Column(nullable = false)
			    private String username;
			    private String password;
			
			    @Embedded
			    private Address homeAddress;
			}
			----------------------------------------
			@Embeddable
			public class Address {

			    private String street;
			    private String city;
			}
			```
	4. #### 1 : n  / n : m 맵핑
		- 따로 메모
		
	5. #### Cascade

	6. #### Fetch모드

	7. #### Query

	8. #### JPA 원리

2. ### 스프링데이터 Common
	1. #### Respository
		1. db 테스트를 위해 h2 의존성 test Scope로 추가
		2. AccountJpaResporsitory Test
		```java
		@DataJpaTest ////슬라이스테스트시 사용하는 스프링부트에서 제공하는 애노테이션
		    //데이터 관련 bean만 생성한다
		    //h2인메모리를 사용하기 때문에 기존 postgres는 사용x 이므로 영향x
		class AccountJpaRepositoryTest {
		   
		    @Autowired
		    AccountJpaRepository accountJpaRepository;
		
		    @Test
		    @DisplayName("객체 저장 후 값 확인하기")
		    public void crudRepository(){
		        //Given
		        Account account = new Account();
		        account.setUsername("young");
		        assertNull(account.getId());
		
		        //When
		        Account newAcoount = accountJpaRepository.save(account);
		
		        //Then
		        assertNotNull(newAcoount.getId());
		    }
		}

		    @Test
		    @DisplayName("페이징테스트")
		    public void pageTest(){
		        //Given
		        Account account = new Account();
		        account.setUsername("youngggggg");
		        accountJpaRepository.save(account);
		
		        //when
		        Page<Account> page = accountJpaRepository.findAll(PageRequest.of(0,10));
		        //db에 있는 account를 내가 정한 규격(0부터 10개)에 맞춰 page로 받아와 다룰 수 있다.

		        //then
		        assertTrue(page.getTotalElements()==1l); //page안에 들어있는 Account 개수
		        assertTrue(page.getNumber()==0); //가져온 현재 page의 번호
		        assertTrue(page.getSize()==10); //앞에서 설정한 들어갈 수 있는 개수
		        System.out.println("========================");
		        System.out.println(page.getTotalElements());
		        System.out.println(page.getNumber());
		        System.out.println(page.getSize());
		
		```
