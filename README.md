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
2. 스프링부트의 의존성
3. 스프링부트의 원리
	1. 자동설정 원리
	2. 내장 웹 서버 이해/응용(1부-컨테이너와 서버포트/2부- http1.1 , http2 , https)

# Springboot-mvc
---
0. Junit test 부분
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

	@Test
	    public void hello() throws Exception{
	        mockMvc.perform(MockMvcRequestBuilders.get("/hello"))
	                .andExpect(MockMvcResultMatchers.status().isOk())
	                .andExpect(MockMvcResultMatchers.content().string("hello"));
	    }
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
	@WebMvcTest(UserController.class)
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
