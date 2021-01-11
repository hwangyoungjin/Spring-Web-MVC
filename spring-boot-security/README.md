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