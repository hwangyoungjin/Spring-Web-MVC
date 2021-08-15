## 농촌진흥청 Open API 활용
---
1. ### [Open API Access key 신청](https://www.nongsaro.go.kr/portal/ps/psz/psza/contentNsMain.ps?menuId=PS03954)

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
	- 1. application-secret.properties에 key값 추가
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

```http
http://api.nongsaro.go.kr/sample/rest/garden/gardenList.jsp?
cntntsNo=&
pageNo=1& // page 번호
word=&
lightChkVal=&
grwhstleChkVal=&
lefcolrChkVal=&
lefmrkChkVal=&
flclrChkVal=&
fmldecolrChkVal=&
ignSeasonChkVal=&
winterLwetChkVal=&
sType=sCntntsSj&
sText=&
wordType=cntntsSj&
priceTypeSel=&
waterCycleSel=
```
 

http://api.nongsaro.go.kr/sample/rest/garden/gardenList.jsp?
cntntsNo=&
pageNo=1&
word=&
lightChkVal=&
grwhstleChkVal=054001%2C054002& 
lefcolrChkVal=&
lefmrkChkVal=&
flclrChkVal=&
fmldecolrChkVal=&
ignSeasonChkVal=&winterLwetChkVal=&sType=sCntntsSj&sText=&wordType=cntntsSj&grwhstleChk=054001&grwhstleChk=054002&priceTypeSel=&waterCycleSel=
