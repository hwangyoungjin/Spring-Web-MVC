## 농촌진흥청 Open API 활용
---
1. ### [Open API Access key 신청](https://www.nongsaro.go.kr/portal/ps/psz/psza/contentNsMain.ps?menuId=PS03954)
	- <img src="https://user-images.githubusercontent.com/60174144/129478104-1981e365-8a24-4d05-b9aa-df3052d26ea6.png" width="70%" height="70%">
	
2. ### 환경
```java
- jdk 8
- gradle
- springboot 2.5.2
- web
```
3. ### RestTemplate 사용
	- 1. 의존성 추가
	```gradle
	dependencies {
		implementation 'org.apache.httpcomponents:httpclient:4.5'
	}
	```
	- 2. Bean 등록
	```java
	@Configuration
	public class HttpClientConfig {
		@Bean
		public RestTemplate restTemplate(){
			HttpComponentsClientHttpRequestFactory factory
					= new HttpComponentsClientHttpRequestFactory();
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

4. ### OPEN API KEY profile 적용
	- 1. application-secret.properties 만들고 key값 추가
	```properties
	nongsaro.openapi.key= ****
	```
	- 2. application.properties에 profiles 설정 [참고](http://honeymon.io/tech/2021/01/16/spring-boot-config-data-migration.html)
	```properties
	#profile
	spring.profiles.include=secret
	```
	- 3. application-secret.properties은 **git.ignore**
	

5. ### 외부파일 사용
	- 1. @ConfigurationProperties 사용하기 위해 
	```gradle
	annotationProcessor "org.springframework.boot:spring-boot-configuration-processor"
	```
	- 참고1. [gradle 의존성 추가](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-configuration-processor/2.5.3)
	- 참고2. [Spring Boot Configuration Annotation...](https://hanke-r.tistory.com/107?category=836499)
	
	- 2. @ConfigurationProperties 적용
	```java
	@Getter
	@Setter
	@Configuration
	@ConfigurationProperties(prefix = "nongsaro.openapi")
	public class NongsaroProperties {
		private String key; //application-secret.properties에 있는 key값
	}
	```
	- 3. 추후 Restemplate 에 들어갈 Url에 사용

6. ### OpenAPI Response 언마샬링 : Xml -> Object 
	```text
	* Spring Boot 에서 기본적으로 포함되어있어 별도의 의존성 추가 없이 바로 사용 가능
	```
	- 1. #### 식물 리스트 반환 형식 [~/lightList?apiKey=***]
		```xml
		<response>
			<header>
				<resultCode>00</resultCode>
				<resultMsg>정상적으로 처리되었습니다.</resultMsg>
				<requestParameter/>
			</header>
			<body>
				<items>
					<item>
						<code>
							<![CDATA[ 055001 ]]>
						</code>
						<codeNm>
							<![CDATA[ 낮은 광도(300~800 Lux) ]]>
						</codeNm>
					</item>
					<item>
						<code>
							<![CDATA[ 055002 ]]>
						</code>
						<codeNm>
							<![CDATA[ 중간 광도(800~1,500 Lux) ]]>
						</codeNm>
					</item>
					<item>
						<code>
							<![CDATA[ 055003 ]]>
						</code>
						<codeNm>
							<![CDATA[ 높은 광도(1,500~10,000 Lux) ]]>
						</codeNm>
					</item>
				</items>
			</body>
		</response>
		```

		- 1. **RootModel**
		```java
		@Getter
		@Setter
		@XmlRootElement(name = "response")
		public class OpenApiResponse {
			private OpenApiHeader header;
			private OpenApiBody body;
		}
		```
		- 2. **Header와 Body Model**
		```java
		@Data
		@XmlRootElement(name = "header")
		public class OpenApiHeader {
			private String resultCode;
			private String resulMsg;
			private String requestParameter;
		}

		@XmlRootElement(name = "body")
		public class OpenApiBody {

			private List<OpenApItem> items;

			/**items의 들어가는 item을 getter에 지정**/
			@XmlElementWrapper(name = "items")
			@XmlElement(name = "item")
			public List<OpenApItem> getItems(){ 
				return items;
			}

			public void setItems(List<OpenApItem> items) {
				this.items = items;
			}
		}
		```
		- 3. **Item Model**
		```java
		@Data
		@XmlRootElement(name = "item")
		public class OpenApItem {
			//garendList
			private String cntntsNo;
			private String cntntsSj;
			private String rtnFileSeCode;
			private String rtnFileSn;
			private String rtnOrginlFileNm;
			private String rtnStreFileNm;
			private String rtnFileCours;
			private String rtnImageDc;
			private String rtnThumbFileNm;
			private String rtnImgSeCode;
		}
		```
	- 2. #### 식물 반환 형식 [~/gardenDtl?apiKey=***&cntntsNo=***]
		```xml
		<response>
			<header>
				<resultCode>00</resultCode>
				<resultMsg>정상적으로 처리되었습니다.</resultMsg>
				<requestParameter>
				<cntntsNo>12938</cntntsNo>
				</requestParameter>
			</header>
			<body>
				<item>
					<adviseInfo>
						<![CDATA[ 식용, 지피, 약용, 향기,관엽, 관화, 관실 ]]>
					</adviseInfo>
					<clCodeNm>
						<![CDATA[ 잎&꽃보기식물,열매보기식물 ]]>
					</clCodeNm>
					<cntntsNo>
						<![CDATA[ 12938 ]]>
					</cntntsNo>
						*
						*
						*
				</item>
			</body>
		</response>
		```
		- 1. **Items 존재 X 이므로 OpenApiBody 부분에 Item 필드 추가**
		```java
		@XmlRootElement(name = "body")
		public class OpenApiBody {

			private List<OpenApItem> items;

			@XmlElementWrapper(name = "items")
			@XmlElement(name = "item")
			public List<OpenApItem> getItems(){
				return items;
			}

			public void setItems(List<OpenApItem> items) {
				this.items = items;
			}

			/**새로 추가**/
			private OpenApItem item; 

			public OpenApItem getItem() {
				return item;
			}

			public void setItem(OpenApItem item) {
				this.item = item;
			}
		}
		```
7. ### 농사로 API 통신
	- 1. Repository
	```java
	@Repository
	@RequiredArgsConstructor
	public class GardenRepository {

		private final RestTemplate restTemplate;

		private final NongsaroProperties nongsaroProperties;

		static String mainUrl = "http://api.nongsaro.go.kr/service/garden/";

		public List<OpenApItem> getGardenListOpenApiByPageNum(int pageNum) {
			System.out.println("key = "+nongsaroProperties.getKey());
			String url = mainUrl+"gardenList?apiKey="+nongsaroProperties.getKey()+"&pageNum="+pageNum;

			/**OpenApiResponse 클래스로 언마샬링**/
			OpenApiResponse response = restTemplate.getForObject(URI.create(url), OpenApiResponse.class);
			return response.getBody().getItems();
		}
		public OpenApItem getGardenDtlOpenApi(String cntntsNo) {
			System.out.println("key = "+nongsaroProperties.getKey());
			String url = mainUrl+"gardenDtl?apiKey="+nongsaroProperties.getKey()+"&cntntsNo="+cntntsNo;

			/**OpenApiResponse 클래스로 언마샬링**/
			OpenApiResponse response = restTemplate.getForObject(URI.create(url), OpenApiResponse.class);
			return response.getBody().getItem();
		}
	}
	```
8. ### 이후 Controller, Service 만들어서 api 값 활용