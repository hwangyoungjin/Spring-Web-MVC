# [Spring-boot Study](https://brunch.co.kr/@springboot/530)
---
# 1주차
## [간단한 API서버 만들기](https://brunch.co.kr/@springboot/531)
---
```java
* 과제
1. Annotation 정리
  * Spring web
    - @RestController
    - @RequestMapping
    - @GetMapping

  * @Component
    - @Repository
    - @Service
    - @Controller
    - @Configuration
        - @ConfigurationProperties

  * Lombok
    - @Getter
    - @Setter
    - @Data
    - @Builder
    - @AllArgsConstructor
    - @NoArgsConstructor

2. DI에 대해서



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
- #### **API 서버의 시스템 구성도**
<img src="https://user-images.githubusercontent.com/60174144/104416698-927d6680-55b7-11eb-870b-7d8be4e94696.png" width="70%" height="70%">

1. ### **[네이버 오픈 API 활용하기](https://developers.naver.com/docs/common/openapiguide/apilist.md#%EA%B2%80%EC%83%89)**
	- 사용 api : **검색**


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
		public class NaverProperties {
		    private String movieUrl;
		    private String clientId;
		    private String clientSecret;
		}
		```

	4. #### **영화 검색 서비스 구현하기**
		- 네이버 오픈 api의 결과를 응답받을 수 있는 객체를 정의
		```java
		@Getter
		@Setter
		@AllArgsConstructor
		@NoArgsConstructor
		public class ResponseMovie {
		    private List<Item> items;
		
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
		- Movie, MovieGroup model 추가
		```java
		@Builder
		@Getter
		public class Movie implements Serializable {
		    private String title;
		    private String link;
		    private float userRating;
		}
		********************
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