# Spring-Web-MVC
## 스프링 MVC 핵심 기술 활용

---
1. 인텔리제이 - SpringBoot, Thymeleaf 프로젝트생성

2. Junit4를 사용하기 위해 pom.xml에서 spring boot starter 의존성 수정 [exclusions 주석처리](https://ratseno.tistory.com/75)

3. http 요청 맵핑 (연습문제완료X)

4. Handler Method 
    1. URI 패턴 `GET` 의 파라미터
    	> - @PathVariable
    	> - @MatrixVariable
     
    2. 요청 매개변수 `GET, POST` 의 파라미터
    	> - @RequestParam

    	> - @ModelAttribute + [@Vaildated , BindingResult]
    	>	+ └> **@valid적용안됨 [dependency추가로 해결](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/2.3.3.RELEASE)**

    	> - BindingResult : 바인딩 에러 발생시 에러표시

     	> - @SessionAttribute
    	>	+ └> Controller 밖(Interceptor or Filter)에서 만든 세션 데이터 접근시 사용
   
    3. POST 후 브라우져 새로고침하는 경우 Redirect이용 : POST-> Redirect -> GET

    4. 모델정보 HttpSession에 저장
    	> - @SessionAttributes(Controller에 설정)으로 모델정보 Session에 등록
     	>	+ └> 파라미터에 @ModelAttribute으로 모델 받아오기
     	>	+ └> 파라미터 SessionStatus 으로 세션 종료

    5. Redirect로 값 전달
    	> - RedirectAttributes의 addAttibute 를 통해 원하는 값전달
     	>	+ └> @RequestParam으로 받아서 값 사용

    	> - RedirectAttributes의 addFlashAttibute 를 통해 원하는 객체전달 
     	>	+ └> Model으로 받아와서 model.asMap().get()으로 사용

    5. File업로드시 사용하는 MultipartFile
    	> - @RequestParam으로 받아서 값 사용

    6. ResponseEntity를 사용하여 File 다운로드
    	> - 응답헤더의 (파일이름, Type, 크기) 명시

    	> - ResourceLoader사용 -> path 설정 -> .getFile()

    	> - [Tika](https://mvnrepository.com/artifact/org.apache.tika/tika-core)로 미디어타입(파일종류) 알아내기

    7. @RequestBody 와 HttpEntity
    8. @ResponseBody 와 ResponseEntity<T>
    9. @ModelAttibute
    10. @InitBinder
    11. @ExceptioHandler

---
---
# Spring-boot 
## (src-Spring-boot)
## 스프링부트 개념과 활용
---
1. 스프링부트 구조
2. 스프링부트의 원리
	1. 의존성 관리
	2. 자동설정 원리
	3. 내장 웹 서버 이해/응용(1부-컨테이너와 서버포트/2부- http1.1 , http2 , https)
	4. 독립적으로 실행 가능한 jar

## Springboot-mvc
---
0. Junit test 부분
	- Junit4 에서 Runwith안되는 경우 Spring boot stater parent버전 2.2.4.RELEASE로 맞추기
	- Junit test에서 
	```java
	>> https://effectivesquid.tistory.com/entry/Spring-test-%EC%99%80-Junit4%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%85%8C%EC%8A%A4%ED%8A%B8
	
	[에러] : get요청 보내기 위해 get 함수 사용시 import 안됨 
	mockMvc.perform(get("/hello"))
	[해결] : MockMvcRequestBuilders class 사용하여 get 요청
	mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
	
	[에러] : 요청에 대한 결과 확인하기 위해 사용하는 status 사용안됨
	.andExpect(status())
	[해결] : MockMvcResultMatchers class 사용하여 status 결과 확인
	.andExpect(MockMvcResultMatchers.status())
	
	[에러] : 요청에 대한 결과 출력하기 위해 사용되는 print()안됨
	.andDo(print())
	[해결] : MockMvcResultHandler class 사용하여 print()
 	
	* 정리 *
	@Test
	    public void hello() throws Exception{
	        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
	                .andExpect(MockMvcResultMatchers.status().isOk())
		  .andDo(MockMvcResultHandlers.print())
	                .andExpect(MockMvcResultMatchers.content().string("hello"));
	    }

	------

	[에러] view에서 특정 String을 포함하는지 검사하는 테스트 containsString()안됨
	.andExpect(MockMvcResultMatchers.content().string(containsString("young")));
	[해결] Matchers class 사용
	.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("young")));
	```
1. 스프링 웹 mvc 1부
	- 스프링 mvc 와 스프링부트 mvc 비교
2. 스프링 웹 mvc 2부
	-  RestController에 json데이터로 Post 테스트하기(JUnit4)
	```java
	[UserController.java]
	@RestController
	public class UserController {

	    @PostMapping("/users/create")
	    public User create(@RequestBody User user){
	        return user;
	    }
	
	[UserControllerTest.java]
	@RunWith(SpringRunner.class)
	@WebMvcTest(UserController.class) // 테스트하려는 class 이름
	public class UserControllerTest {
	
	    //해당 객체는 @WebMvcTest를 통해 자동으로 bean으로 만들어지는데 그 bean을 사용
	    @Autowired
	    MockMvc mockMvc;
	  
	    //User를 생성하여 테스트
	    @Test
	    public void createUser_JSON() throws Exception {
	        String userJson = "{\"username\":\"young\",\"password\":\"123\"}";
	        mockMvc.perform(MockMvcRequestBuilders.post("/users/create") // 해당 url로
	                .contentType(MediaType.APPLICATION_JSON) //
	                .accept(MediaType.APPLICATION_JSON) //받고싶은 데이터타입 명시 안해도 상관없으나 주면 더 좋음
	                .content(userJson)) // json형식의 데이터를 request로 보낸다.
		        // response가 정상적으로 200 나오는지 확인
	                    .andExpect(MockMvcResultMatchers.status().isOk())
	                    //응답결과가 json으로 나올것이므로 본문 username, password에 내가 넣어주었던 값이 young,123이 나오는지 확인
	                    .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("young"))
	                    .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123"));
	    }
	```
3. 스프링 웹 mvc 3부
	- ViewResolver
4. 스프링 웹 mvc 4부
	- 정적리소스 지원
		- 기본리소스 classpath 제공
		- WebMvcConfiguration의 addResourceHandlers로 핸들러 추가 가능
	- 컴파일 빌드 개념 참고
	```html
	* 컴파일
	: 컴파일이란 개발자가 작성한 소스코드를 바이너리 코드로 변환하는 과정을 말한다. (목적파일이 생성됨) 

	* 링크
	: 소스파일 A,B있을때 A의 함수가 B를 필요로 한다면 컴파일 결과물중에서 링크가 이를 연결
	: 정적링크란 컴파일된 소스파일을 연결해서 실행가능한 파일을 만드는 것이고
	: 동적링크란 프로그램 실행 도중 프로그램 외부에 존재하는 코드를 찾아서 연결하는 작업
	: 자바의 경우, JVM이 프로그램 실행 도중 필요한 클래스를 찾아서 클래스패스에 로드해주는데 이는 동적링크의 예

	* 빌드(Build) [IntelliJ에서 ctrl + F9]
	: 소스코드 파일을 실행가능한 소프트웨어 산출물로 만드는 일련의 과정을 말한다. 
	: 빌드의 단계 중 컴파일이 포함이 되어 있는데 컴파일은 빌드의 부분집합이라 할 수 있다.
	: 빌드를 도와주는 빌드 툴 :  Ant, Maven, Gradle 등
	: 빌드 툴은 전처리(preprocessing), 컴파일(Compile), 패키징(packaging), 테스팅(testing), 배포(distribution)를 지원
	```
5. 스프링 웹 mvc 5부
	- 스프링부트는 client에서 사용하는 라이브러리(jquery, bootstap, ReactJS 등등)  jar파일로 추가가능
	- [Maven 중앙저장소](https://mvnrepository.com/)에서 해당 의존성 pop.xml에 추가
6. 스프링 웹 mvc 6부
	- 스프링의 root url "/" 은 기본적으로 index.html 이므로 기본 resource directory에 index.html 파일을 넣는다.
	- 파비콘은 favicon.icon 확장자로 기본 resource directory에 넣는다.
		- cache때문에 변경 안 될 수 있다. /favicon.ico 요청 후 다시 localhost 요청시 해결가능
7. 스프링 웹 mvc 7부
	- 스프링부트가 지원하는 템플릿 엔진 중 하나인 Thymeleaf 사용하기
		1. pom.xml에 Thymeleaf 의존성 추가
		```java
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-thymeleaf</artifactId>
		</dependency>
		```
		2. 모든 동적으로 생성하는 view는 /src/main/resources/template/ 에 넣는다.
			- **Thymeleaf 템플릿 엔진(servlet Container와 독립적)이 이를 처리**
8. 스프링 웹 mvc 8부 
	- HtmlUnit
9. 스프링 웹 mvc 9부
	- ExceptionHandler
		- 스프링부트가 제공하는 기본 예외처리 핸들러 **BasicErrorController**
		- 커스터마이징 : BasicErrorController를 상속받아 구현하기
	- 커스텀 에러 페이지
		- status code 별로 resource/static/error Or resource/template/error에 페이지 구성
		- 페이지 명은 status code 이름으로 만든다. (Ex> 404.html)
10. 스프링 웹 mvc 10부
	- SpringHATEOAS 아직이해X
11. 스프링 웹 mvc 11부
	- CORS : Cross-Origin-Resource-Sharing
		- 서로다른 origin끼리 Resource Sharing 가능
		- Sharing 해주는 쪽(port: 8080) @Controller Or @RequestMapping 쪽에 @CrossOrigin(oring정보:18080) 입력
		```java
		    @CrossOrigin(origins = "http://localhost:18080")
		    @GetMapping("/hello")
		    public String hello() {
		        return "hello";
		    }
		``` 

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

## Springboot-Security
---
1. 준비 : Thymeleaf로 view rendering 사용
	- Thymeleaf 의존성 추가
	- controller 생성 ( GetMapping : hello, my )
	- view 3개 생성 ( index.html / hello.html / my.html )
	- test 완료
	```java
	@RunWith(SpringRunner.class)
	@WebMvcTest(HomeController.class)
	public class HomeControllerTest {
	
	    @Autowired
	    MockMvc mockMvc;
	
	    @Test
	    public void hello() throws Exception{
	        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
	                .andDo(MockMvcResultHandlers.print())
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.view().name("hello"));
	    }
	
	    @Test
	    public void my() throws Exception{
	        mockMvc.perform(MockMvcRequestBuilders.get("/my"))
	                .andDo(MockMvcResultHandlers.print())
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.view().name("my"));
	    }
		
	}
	```
2. Spring Security 사용
	- Security 의존성 추가
	- test 재실행 -> 오류
	```java
	[에러메세지]
	: MockHttpServletResponse:
	  Status = 401
	  Error message = Unauthorized
	  Headers = [WWW-Authenticate:"Basic realm="Realm"", -------
	[이유]
	: 스프링부트가 제공하는 시큐리티 자동설정 중 하나로
	: 모든 요청(request) 모두 인증이 필요
	  -> Basic, form 요청 인증 필요

	[해결] 
	1. security test 의존성 추가
	        <dependency>
	            <groupId>org.springframework.security</groupId>
	            <artifactId>spring-security-test</artifactId>
	            <version>${spring-security.version}</version>
	        </dependency>
	2. test 메소드에 @WithMockUser 추가
	```
	- Springboot에서 security 관련하여 제공하는 자동설정 1
		- **SecurityAutoConfiguration.java**
		- SpringSecurity 의존성이 들어있는 경우 적용된다.
		- 자동으로 기본 login form 만들어 준다
		- 따라서 브라우져로 인증 안된 요청시 localhost:8080/login으로 자동 redirect 된다. 
		
	- Springboot에서 security 관련하여 제공하는 자동설정 2
		- **WebSecurityConfigurerAdapter.java**
		- 해당 class 를 상속받으면 Springboot가 제공하는 Security와 거의 동일한 나만의 security 만들 수 있다.
		```java
		* 제공되는 spring Security 기본설정 핵심
		* WebSecurityConfigurerAdapter.java 안에 configure() 메소드
		protected void configure(HttpSecurity http) throws Exception {
		        this.logger.debug("Using default configure(HttpSecurity). If subclassed this will potentially override subclass configure(HttpSecurity).");
		        ((HttpSecurity)
			((HttpSecurity)((AuthorizedUrl)http
			     .authorizeRequests() // 권한 설정하기
			     .anyRequest()) // 모든 요청에 대해서
			.authenticated() // 인증이 필요
			.and())
		        .formLogin() // formLogin을 사용하겠다.
		        .and())
		        .httpBasic(); // httpBasic Authentication을 사용하겠다.
		    }
		
		**Basic Authentication이란?
		   : REST API 인증을 할 때, username, password로 인증을 하는 방법
		```
	- Springboot에서 security 관련하여 제공하는 자동설정 3
		- **UserDetailsServiceAutoConfiguration.java**
		- 스프링부트 application이 실행될때 생성되는 인메모리 user 객체를 만들어 제공
		- username은 **user** , password는 매번 **application 띄울때마다** 달라진다.
		``` java
		but, 아래 3개 interface을 구현한 bean이 없을때만 만들어준다. 
		- interface AuthenticationManager
		- interface AuthenticationProvider 
		- interface UserDetailsService
		
		cf. 스프링 시큐리티를 사용하는 프로젝트에선 100% 인메모리 user객체를 사용하지 않으므로
		사용할 일 거의 없다.
		```
3. Spring Security 커스터마이징
	- 기존 view에서 home ("/")인 index.html은 인증없이 접근 가능하도록
		1. 자동설정 2의 **WebSecurityConfigurerAdapter.java**를 상속받기[참조](https://victorydntmd.tistory.com/328)
		2. 기존 자동설정인 **SecurityAutoConfiguration.java** 자동설정안됨
		3. **PasswordEncoder** 사용하기
		```java
		@Configuration
		public class SecurityConfig extends WebSecurityConfigurerAdapter {
		    @Override
		    protected void configure(HttpSecurity http) throws Exception {
		    // HttpSecurity를 통해 HTTP 요청에 대한 웹 기반 보안을 구성
		        http.authorizeRequests()  // 모든 요청에 대해 접근 설정
		                .antMatchers("/","/hello").permitAll() // antMatchers() 메서드로 url은 승인(permitAll())
		                .anyRequest().authenticated() // 이외 나머지 요청은 권한필요
		                .and()
		                .formLogin() 
			    //security가 제공하는 로그인 폼 사용, form 기반으로 인증
			    //로그인 정보는 기본적으로 HttpSession을 이용
			    //login 경로로 접근하면, Spring Security에서 제공하는 로그인 form을 사용가능
			    //기본 제공되는 form 말고, 커스텀 로그인 폼을 사용하고 싶으면 loginPage() 메서드를 사용합니다.
			    //이 때 커스텀 로그인 form의 action 경로와 loginPage()의 파라미터 경로가 일치해야 인증을 처리할 수 있습니다. ( login.html에서 확인 )
		                .and()
		                .httpBasic(); //http basic 로그인 권한 (authenticated) 사용
		    }

		    //passwordEncoder를 통하여 account 객체 저장시 pw 암호화하여 저장가능
		    @Bean
		    public PasswordEncoder passwordEncoder(){
		        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		    }
		}
		```
	- Account domain 만들기 @Entiry, @id, @GeneratedValue로 JPA연결 
	- jpa, h2 의존성 추가 (임베디드 인메모리 데이타 베이스 사용)
	- AccountRepository 만들어서 jpa상속
	- AccountService 만들기
		1. account 저장 [**passwordEncoder 사용**]
		2. springSecurity에서 두번째로 중요한 UserDetailsService 인터페이스 구현
			- 보통 유저정보 관리하는 @Service 계층에서 사용해야한다
			- 구현한 것이 Bean으로 등록되어 있어야 스프링부트가 만들어주는 유저정보 생성X
		```java
		* UserDetails : 유저정보를가저와 담는 VO정도의 역할로 사용
		@Service
		public class AccountService implements UserDetailsService {
		    @Autowired
		    private AccountRepository accountRepository;

		    @Autowired
		    private PasswordEncoder passwordEncoder;

		    public Account createAccont(String username, String password){
		        Account accout = new Account();
		        accout.setUsername(username);
		        accout.setPassword(passwordEncoder.encode(password));
		        return accountRepository.save(accout);//만들쪽에서 받아서 사용가능하도록 return
		    }
		
		    @Override
		    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		        //spring security 로그인 처리시 호줄되는 메서드
		        //로그인시 입력하는 id가 매개변수 username으로 들어와
		        //username에 해당하는 user정보를 확인해서 해당하는 password와 입력한 pw가 같은지 확인
		        //같다면 로그인처리, 다르다면 예외던진다. 해당 예외를 처리하면서 비번 틀리다는 메시지 보낼 수 있다.
		
		        //Optional 클래스는 null이나 null이 아닌 값을 담을 수 있는 클래스
		        Optional<Account> byUsername = accountRepository.findbyUsername(username);
		
		        //찾은 username이 존재하지 않는 다면 UsernameNotFoundException 예외발생시키기, 존재하면 account 타입으로 받기
		        Account account = byUsername.orElseThrow(()->new UsernameNotFoundException(username));
		
		        //UserDetails 타입으로 return -> 스프링시큐리티에서 UserDetails타입의 기본구현체를 User라는 이름으로 제공한다. 이를통해 인증된 객체 리턴
		        //cf. UserDetails 인터페이스를 상속받아 커스텀 가능
		        return new User(account.getUsername(),account.getPassword(),authorities());
		    }
	
		    private Collection<? extends GrantedAuthority> authorities() {
		        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
		    }
		}
		```
		- ![spring-Security](https://user-images.githubusercontent.com/60174144/103727757-6d20b380-501f-11eb-9ec5-de0a34e9dabb.png)

	- applicationRunner를 상속받아 초기화 코드에 user객체 하나 만들기 
	```java
	@Component
	public class AccountRunner implements ApplicationRunner {
                
	 @Autowired
	 AccountService accountService;
                
	 @Override
	 public void run(ApplicationArguments args) throws Exception {
	    Account young = accountService.createAccont("young","1234");
	    System.out.println(young.getUsername()+"password: "+ young.getPassword());
	 }
	}
	```	 
	```java
	* 어처구니없는 오류
	[오류]
	UnsatisfiedDependencyException
	[원인]
	AccountRepository.java에서
	@Repository
	public interface AccountRepository extends JpaRepository<Account,Long> {
	    Optional<Account> findbyUsername(String username);
	}

	[해결]
	findbyUsername -> findByUsername 로 b가 문제였음
	B로 바꿔서 해결완료
	```
4. 인증된 User Controller에서 활용
	- 2가지 (**Authentication 파라미터** or **전역변수**)
	```java
	@GetMapping("/my")
	public String my(Model model, Authentication authentication){
	    model.addAttribute("name",authentication.getName());
	
	    Authentication a = SecurityContextHolder.getContext().getAuthentication();
	    model.addAttribute("name1",a.getName());
	    return "my";
	}
	``` 

# Springboot- Rest Client (어플리케이션 안에서 서버에 REST HTTP 요청 할 때 사용)
---
- 스프링부트가 rest Client에 대해서 직접적인 기능 제공은 x
- 스프링 프레임워크에서 rest Client 쉽게 사용가능하도록 Bean 제공
	- RestTemplateBuilder Bean등록
	- WebClient.Builder  Bean등록
	- **Builder를 주입받아서 필요할때마다 Build로 rest Client 인스턴스생성 사용**
- **RestTemplate** [동작원리](https://sjh836.tistory.com/141)
	- spring 3.0 부터 지원
	- web 의존성 필요
	- Blocking I/O 기반의 Synchronous API
	- 스프링에서 제공하는 http 통신에 유용하게 쓸 수 있는 템플릿
	- HTTP 서버와의 통신을 단순화하고 RESTful 원칙을 지킨다
	```java
	*기본 사용 방법
	@Autowired
	RestTemplateBuilder restTemplateBuilder;

	RestTemplate restTemplate = restTemplateBuilder.build(); //빌더를 통해 빌드 하여 restTemplate인스턴스 받기
	String helloResult  = restTemplate.getForEntity("http://localhost:8080/hello", String.class); // url호출하여 결과값받기
	
	* 이때 /hello 받는 컨트롤러는 @RestController
	```
- **WebClient [추천]**
	- RestTemplate 의 대안으로 Spring 에서는 WebClient 사용을 강력히 권고
	- Webflux 의존성 필요
	- Non-Blocking I/O 기반의 Asychronous API
	```java
	* 기본 방법

	@Autowired
	WebClient.Builder builder;

	@Test
	void test1() {
	
	    WebClient webClient = builder();
	    Mono<String> hello = webClient.get()
	            .uri(""http://localhost:8080/hello")
	            .retrieve()
	            .bodyToMono(String.class);
	
	    StepVerifier.create(hello)
	            .expectNext("hello!")
	            .verifyComplete();
	}
	
	* 이때 /hello 받는 컨트롤러는 @RestController
	```

## Spring boot 운영
---
1. 스프링부트는 Actuator라는 모듈 제공
	- 스프링부트 운영중에 주시할 수 있는 유용한 정보들 제공
	- 정보들을 EndPoint를 통해 제공해준다.
	```java
	* EndPoint : 통신의 끝점
	```
	![image](https://user-images.githubusercontent.com/60174144/104096264-cc601b80-52de-11eb-8e75-f49af0715ea2.png)
	- EndPoint를 통해 확인할 수 있는 정보 흭득하기 
		1. Spring-boot-starter-actuator 의존성 추가
		2. [해당 문서](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints)에서 나온 모든 ID에 해당하는 Actuator들 활성화 된다.
		```java
		* auditevents : 누가 인증에 성공하고 실패 했는지에 대한 정보
		* beans : 컨테이너에 등록된 빈들 정보
		* conditions : 어떤 자동설정이 어떤 조건에 의해 설정되고 설정안되었는지 정보
		* configprops : application.properties에 정의가 가능한 것들 
		* httptrace : 최근 100개의 http 요청과 응답 정보
		* logger : 패키지의 로깅레벨
		* matrics : application의 핵심 정보 ( app이 사용하는 메모리, cpu 등)
		* mappings : controller 매핑 정보
		* shotdown (유일하게 비활성화 되어있다.) 
		등등
		```	
2. Spring-boot-admin
	- [참고](https://jaehyun8719.github.io/2019/06/20/springboot/admin/)
	

