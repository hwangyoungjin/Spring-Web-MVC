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