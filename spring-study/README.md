# [Spring-boot Study](https://brunch.co.kr/@springboot/530)
---
# 1주차
## [간단한 API서버 만들기](https://brunch.co.kr/@springboot/531)
---
```java
* 환경
- 2.3.7 RELEASE
- Lombok의존성 추가 
- gradle
```
- #### **API 서버의 시스템 구성도**
![image](https://user-images.githubusercontent.com/60174144/104263075-2d991200-54cc-11eb-8d38-b81ec79e5e05.png)

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
		                .getBody()
		                .getItems()
		                .stream()
		                .map(m->Movie.builder()
		                    .title(m.getTitle())
		                    .link(m.getLink())
		                    .userRating(m.getUserRating())
		                    .build())
		                .collect(Collectors.toList());
		    }
		}
		```		



