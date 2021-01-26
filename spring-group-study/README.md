 # [Spring-boot Study](https://github.com/sieunkr/spring-study-group)
---

### 목차
1. 1주차 : [간단한 API서버 만들기](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-group-study/README.md#1주차)
2. 2주차 : [스프링부트 AutoConfiguration](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-group-study/README.md#2주차)

## 1주차
### [간단한 API서버 만들기](https://brunch.co.kr/@springboot/531)
---
### 1. Annotation 정리 
```java
  * @ComponentScan : Bean 설정을 위해 스캔할 범위를 지정
  * @Component : 개발자가 직접 작성한 class를 스프링 IoC Container에 Bean으로 등록하기 위한 어노테이션 
    - @Repository : DB나 file같은 외부 I/O를 처리하는 클래스에 사용하여 Bean으로 등록
    - @Service : 웹 내부에서 서비스를 처리하는 클래스에 사용하여 Bean으로 등록
    - @Controller : 웹요청과 응답을 처리하는 클래스에 사용하여 Bean으로 등록
    - @Configuration : 스프링 IoC Container에게 해당 클래스가 Bean구성 class임을 알려주는 어노테이션
        
  * 외부설정
    - @ConfigurationProperties : 자바 Bean 스펙을 따라서 *.properties , *.yml 파일에 있는 값을 해당 어노테이션 붙은 클래스에 Getter, Setter를 이용하여 필드값의 바인딩 해준다.
	1. 사용하기 위해선 configuration-processor 의존성 추가필요
	2. @Configuration과 같이 사용
	3. prefix="name" 사용시 *.properties에서 해당"name"으로 시작하는 값만 클래스 field에 바인딩 된다.

  * Spring web
    - @RequsetBody :  요청본문에 들어있는 데이터를 HttpMessageConverter를 통해 자바 객체로 받아올 때 사용
	1. @Valid or @Validated를 사용하여 값 검증 가능
	2. BindingResult 아규먼트를 사용해 코드로 바인딩 또는 검증 에러를 확인 가능

    - @ResponseBody : 자바 객체의 데이터를 HttpMessageConverter를 사용해 응답 본문 메시지로 변환하여 보낼 때 사용

    - @RestController : @Controller 어노테이션과 @ResponseBody 어노테이션을 합쳐놓은 어노테이션 
	1. 자동으로 모든 핸들러 메소드에 @ResponseBody 적용하여 메소드마다 선언하지 않아도 된다.
	2. @Controller는 viewPage를 반환하지만 @RestController는 객체를 반환하면 변환되어 바로 응답으로

    - @RequestMapping : 스프링 웹 MVC에서 HttpMethod 맵핑하는 Annotation
	1. class,method Level에서 사용
	2. class에 선언되어 있다면 선언한  url 패턴뒤에 이어 붙여서 맵핑
	3. 특정 HttpMethod 설정
	  Ex> @RequestMapping(method = RequestMethod.GET)
	3. 특정한 타입의 데이터 담고 있는 요청만 처리하는 핸들러의 경우
	  Ex> @RequestMapping(consumes=MediaType.APPLICATION_JSON_UTF8_VALUE)
	4. 특정한 타입의 응답을 만드는 핸들러의 경우
	  Ex> @RequesMapping(produces="application/json")
	5. 특정한 헤더가 있는 요청을 처리하고 싶은 경우
	  Ex> @RequesMapping(headers="key")
	6. 특정한 요청 매개변수 키를 가지고 있는 요청을 처리하고 싶은 경우
	  Ex> @RequesMapping(params="a")
	 
    - @GetMapping : @RequestMapping과 동일 하지만 GET요청만 맵핑한다.
	- 이외 에도 @PostMapping, @PutMapping, @DeleteMapping 등등 있다.

  * Lombok : 반복적으로 만드는 코드를 어노테이션을 통해 줄여 주는 라이브러리
    - @Getter & @Setter : 필드에 접근자와 설정자를 자동으로 생성해준다.
	1. 클래스 레벨에 설정하는 경우 모든 필드에 접근자와 설정자가 자동으로 생성
 
    - @Builder : Class에 대한 복잡한 Builder API들을 자동으로 생성
	1. 클래스, 생성자, 메서드 레벨에서 사용가능
	2. 빌더패턴 : 객체 생성할때 사용되는 패턴 - 참고 : https://jh-7.tistory.com/3
 
    - @AllArgsConstructor : 모든 필드 값을 파라미터로 받는 생성자를 만들어준다.
    - @NoArgsConstructor : 파라미터가 없는 기본 생성자를 생성해준다.
    - @Data : @Getter, @Setter, @RequireArgsConstuctor, @ToString, @EqualsAndHashCode를 한꺼번에 설정해준다.
```
### 2. DI (Dependenct Injection) : '의존성 주입'에 대해서[Ioc & DI의 개념 & Bean생성방법](https://brunch.co.kr/@springboot/532)
```java 
* IoC (Inversion of Control) : DI라고도 하며 어떤 객체가 사용하는 의존 객체를 직접 만들어 사용하는게 아니라 주입 받아 사용하는 방법을 말한다.
* IoC Container : 애플리케이션 컴포넌트의 중앙 저장소로 빈 설정 소스로 부터 빈 정의를 읽어들이고, 빈을 구성하고 제공한다.
	
* DI 장점
  1. 의존성 주입으로 인해 모듈 간의 결합도가 낮아지고 유연성이 높아진다.
  2. 재사용성이 증가한다.
  3. 더 많은 테스트 코드 만들 수 있다.
  4. 코드 읽기가 쉬워진다.
```
<img src="https://user-images.githubusercontent.com/60174144/104797462-8ab10280-5801-11eb-90a0-8ff3a5988eec.png" width="70%" height="70%">

```java
* 질문사항
1. ResponseMovie없이 Movie객체로 응답 받을 수 는 없는건지?
2. 아래 Logic이 맞는지
```
```java
* 환경
- 2.3.7 RELEASE
- Lombok의존성 추가 
- gradle
```

1. ### **[네이버 오픈 API 활용하기](https://developers.naver.com/docs/common/openapiguide/apilist.md#%EA%B2%80%EC%83%89)**
	- 사용 api : **검색**
	- #### **API 서버의 시스템 구성도**
	- <img src="https://user-images.githubusercontent.com/60174144/104416698-927d6680-55b7-11eb-870b-7d8be4e94696.png" width="70%" height="70%">


2. ### **API 서버 구축하기**
	```java
	[ directory 구조 ]
	* config
	  - HttpClientConfig
	  - NaverProperies
	* model
	  - Movie
	  - MovieGroup
	  - ReponseMovie
	     - Item
	* Service
	  - MovieService
	* repository
	  - MovieRepository
	* repositoryImpl
	  - MovieRepositoryImpl
	* controller
	  - SearchController
	``` 

	1. #### **restTemplate 사용**
		- RestClicent 사용하기 위해 의존성 추가
		```java
		implementation 'org.apache.httpcomponents:httpclient:4.5'
		```
		- RestTemplate @Bean 설정
		```java
		@Configuration
		public class HttpClientConfig {
		    @Bean
		    public RestTemplate restTemplate(){
		        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		        HttpClient httpClient = HttpClientBuilder.create()
		                .setMaxConnTotal(50)
		                .setMaxConnPerRoute(10)
		                .build();
		
		        factory.setHttpClient(httpClient);
		        factory.setConnectTimeout(3000);
		        factory.setReadTimeout(5000);
		        return new RestTemplate(factory);
		    }
		}
		```

	2. #### **Active Profiles**
		- Naver OpenAPI를 호출하기 위해 필요한 정보
		- resource 안 application-sercre.properties에 추가 (**git.ignore**)
	3. #### **Config 설정**
		- naver api를 사용하기 위하여 prefix를 naver.openapi로 설정
		```java
		@Getter
		@Setter
		@Configuration
		@ConfigurationProperties(prefix = "naver.openapi") 
		//*.properties에서 앞에 naver.openapi로 시작하는 값만 가져와서 바인딩
		public class NaverProperties {
		    private String movieUrl; //application.properties에 있는 value에 해당
		    private String clientId; //application-secret.properties에 있는 value에 해당
		    private String clientSecret; //application-secret.properties에 있는 value에 해당
		}
		```

	4. #### **영화 검색 서비스 구현하기**
		- 네이버 오픈 api의 결과를 받아 바인딩할 객체 정의
		```java
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public class ResponseMovie {
		    private List<Item> items;
		
		    @Getter
		    @Setter
		    @AllArgsConstructor
		    @NoArgsConstructor			
		    public static class Item{
		        private String title;
		        private String link;
		        private String actor;
		        private String director;
		        private float userRating;
		        //TODO: 필드추가
		    }
		}

		*Lombok Annotation 인텔리제이에서 자동추가 안되는 문제
		-> 직접 import lombok.Annotation; 해줌으로써 해결
		```

		- 직접 사용할 Movie 객체 정의
		```java
		@Builder
		@Getter
		public class Movie implements Serializable {
		    private String title;
		    private String link;
		    private float userRating;
		}
		```

		- MovieRepository 인터페이스 정의
		```java
		@Repository
		public interface MovieRepository {
		    List<Movie> findByQuery(String query);
		}
		```

		- MovieRepository 구현 : MovieRepositoryImpl
		```java

		* never open api를 통해 데이터 가져오는 부분 
		* restTamplate의 경우 map 또는 사용자가 정의한 class 등 다양한 형태로 데이터를 바로 파싱해서 받는다
		* RestTemplate Method :  exchange 
		  - HttpMethod : Any
		  - 설명 : 헤더세팅해서 HttpMethod 요청 보내고 HttpMessageConverter를 통해         
		          인자로 넘긴 Object타입으로 json 데이터 변환해서 ResponseEntitiy로 받는다.
		  - Parameter
		     1. url
		     2. HttpMethod
		     3. HttpEntity (requestEntity)
		     4. Object (Class responseType)

		@Component
		public class MovieRepositoryImpl implements MovieRepository {
		    @Autowired
		    private RestTemplate restTemplate;
		    @Autowired
		    private NaverProperties naverProperties;
		    @Override
		    public List<Movie> findByQuery(String query) {
		        HttpHeaders httpHeaders = new HttpHeaders();
		        httpHeaders.add("X-Naver-Client-Id",naverProperties.getClientId());
		        httpHeaders.add("X-Naver-Client-Secret",naverProperties.getClientSecret());
		
		        String url = naverProperties.getMovieUrl()+"?query=" + query;
		        
		        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(httpHeaders), ResponseMovie.class)
		                .getBody() // 응답 본문
		                .getItems()
		                .stream()
		                .map(m->Movie.builder() // Movie객체에 담는다.
		                    .title(m.getTitle())
		                    .link(m.getLink())
		                    .userRating(m.getUserRating())
		                    .build())
		                .collect(Collectors.toList());
		    }
		}
		```		

		- MovieService에서 Repository 통해 가져온 데이터 응용
		```java
		@Service
		public class MovieService {
		
		    @Autowired
		    MovieRepository movieRepository;
		
		    public List<Movie> search(final String query){
		        return movieRepository.findByQuery(query);
		    }
		}
		```

		- SearchController 정의
		```java
		@RestController
		@RequestMapping("/api/v1/search")
		public class SearchController {
		    @Autowired
		    private MovieService movieService;
		
		    @GetMapping("/movies")
		    public List<Movie> getMoviesByQuery (@RequestParam(name = "q") String query){
		         return movieService.search(query);
		    }
		}
		```

		- WebBrower에서 JSON 데이터형식 응답 확인
		- <img src="https://user-images.githubusercontent.com/60174144/104403195-5805d080-559b-11eb-8362-0a34f8b7e4b1.png" width="70%" height="70%">

		- 평점순으로 정렬하기 위해 [NAVER 스펙 확인](https://developers.naver.com/docs/search/movie/)
			- 평점 순 정렬기능 제공X

	5. #### **영화 검색 서비스 평점 순 정렬 기능 추가**
		- MovieGroup model 추가 해서 정렬기능 만들기
		```java
		public class MovieGroup {
		    private final List<Movie> list;
		    public MovieGroup(final List<Movie> list){
		        this.list=list;
		    }
		
		    public List<Movie> getList() {
		        return list;
		    }
		
		    public List<Movie> getListOrderRating(){
		        return list.stream().filter(b->!((Float)b.getUserRating()).equals(0.0f))
		                .sorted((a,b)->b.getUserRating() > a.getUserRating() ? 1:-1)
		                .collect(Collectors.toList());
		    }
		}
		```
		- MovieService 수정
		```java
		public List<Movie> search(final String query){
		  MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
		  return movieGroup.getListOrderRating();
		}
		```
		- <img src="https://user-images.githubusercontent.com/60174144/104404088-386fa780-559d-11eb-9ba3-220deb1ce049.png" width="70%" height="70%">
	
	6. #### **영화 데이터 전부 가져올 수 있도록 필드 추가**
		- Movie, ResponseMovie의 item의 필드 추가
		```java
		@Builder
		@Getter
		public class Movie implements Serializable {
		    private String title;
		    private String link;
		    private float userRating;
		    private String image;
		    private String pubDate;
		    private String actor;
		    private String director;
		    //titile, link, userRating, image, pubDate, actor, director
		}
		****************
		public class ResponseMovie {
		    private List<Item> items;

		    @Getter
		    @Setter
		    @AllArgsConstructor
		    @NoArgsConstructor
		    public static class Item{
		        //titile, link, userRating, image, pubDate, actor, director
		        private String title;
		        private String link;
		        private float userRating;
		        private String image;
		        private String pubDate;
		        private String actor;
		        private String director;
		        //TODO: 필드추가
		    }
		}
		```
		- <img src="https://user-images.githubusercontent.com/60174144/104413722-2fd59c00-55b2-11eb-9144-857fec57f751.png" width="70%" height="70%">

## 2주차
### [스프링부트 AutoConfiguration](https://brunch.co.kr/@springboot/533)
---
1. ### 스프링 AutoConfiguration                                  
	1. #### 스프링부트 @SpringBootApplication
	```java
	@SpringBootApplication에는 3가지 Annotation이 포함되어 있다.
	  - @EnableAutoConfiguration : 스프링부트의 AutoConfiguration을 사용하겠다는 어노테이션
	  - @SpringBootConfiguration : 스프링부트에서 @Configuration을 대체하는 스프링부트 필수 어노테이션
	  - @ComponentScan : 해당 어노테이션 이하 파일에서 등록할 Bean 스캔

	* 참고 Annotation
	1. @Conditional : 스프링IoC컨테이너에 조건부로 Bean등록하는 역할 가능하다.
	  - @ConditionOnClass(*.class) : classpath에 해당(*)클래스가 존재하면 Bean등록 (즉, 의존성으로 들어와 있다면 해당 로직 수행된다.)
	  - @ConditionOnMissingClass(*.class) : 해당(*)클래스가 없다면 Bean등록
	  - @ConditionOnBean(*.class) : 해당 Bean(*)이 존재하면 Bean으로 등록
	  - @ConditionOnMissingBean(*.class) : 해당(*)Bean이 없다면 Bean등록
	```
	2. #### @EnableAutoConfiguration의 역할
	```java
	스프링부트에서 Bean을 읽을 때 2단계로 읽혀진다
	1단계. @ComponentScan ()
	2단계. @EnableAutoConfiguration
	
	* @EableAutoConfiguration은
	External Library인 org.springframework.boot.autoconfigure.EnableAutoConfiguration 의
	spring.factoriese에 있는 항목(class)들을 Scan 후 Bean으로 등록하여 자동설정
	```
	3. #### 자동설정을 application.properties를 통해 커스터마이징하기
	```java
	1. @EableAutoConfiguration이 등록한 자동설정의 Bean을 사용할때
	2. 대부분의 Bean Class는 등록된 properties(class)를 읽어온다 
	3. properties(class)에는 @ConfigurationProperties를 사용하여 prefix가 정해져있다.
	4. 따라서 application.properties를 활용하여 코드에 큰 수정없이 prefix값으로 커스터마이징 가능  
	```
2. ### 과제
	1. #### 임베디드 톰캣의 쓰레드 풀 사이즈 변경
		```java
		server.tomcat.threads.max=200 // application.properties에서 변경
		```
	2. naver open API를 통해 영화검색서비스 응답 데이터 필드 모두 추가 + 평점이 0인 데이터는 제외하기
	3. naver open API를 통해 영화 외 다른 검색서비스 추가