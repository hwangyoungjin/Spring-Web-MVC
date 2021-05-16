## Swagger
---

1. ### 환경
```java
- jdk 11
- gradle
- springboot 2.4.3
- devtools
- web
```
2. ### dependency 추가
```gradle
dependencies {
	 // Swagger 2 
	 // https://mvnrepository.com/artifact/io.springfox/springfox-swagger2/2.9.2
	 compile group: 'io.springfox', name: 'springfox-swagger2', version: '2.9.2' 
	 //웹 UI 화면을 보려면, springfox-swagger-ui 를 추가해야 한다.
	 compile group: 'io.springfox', name: 'springfox-swagger-ui', version: '2.9.2' 
}
```

3. ### @EnableSwagger2 추가 
```java
@EnableSwagger2
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
```

4. ### Swagger2 Config 설정
```java

@Configuration
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                /**swagger에서 request URL의 기본이 됨 , 앞에 http or https는 ssh적용 여부에 따라 알아서 됨**/
                .host("cauconnect.com").pathProvider(new RelativePathProvider(servletContext){
                    @Override
                    public String getApplicationBasePath() {
                        return "/api";
                    }
                })
                .select()
                //패키지 경로 설정
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(PathSelectors.any())
                .build();
    }
}


.apis(): API 문서를 만들어줄 범위를 지정한다. 위 예제에서는 com.example.demo 하위 구조를 탐색하여 문서를 생성해준다.
.paths(): API 의 URL 경로를 지정할 수 있다. .paths(PathSelectors.ant("api/v1/**")) 와 같이 하면 http://localhost/api/v1/ 하위 경로를 가지는 API에 대해 문서를 생성해준다.


```

- ### [옵션]
```java
* 부가적으로 API 문서에 대한 내용을 수정하거나 추가하고 싶을때

@Configuration
public class Swagger2Config {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                //패키지 경로 설정
                .apis(RequestHandlerSelectors.basePackage("com.example.demo"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("제목 작성")
                .version("버전 작성")
                .description("설명 작성")
                .license("라이센스 작성")
                .licenseUrl("라이센스 URL 작성")
                .build();
    }
}
```

5. ### RestController 등록
```java
@RestController
@RequestMapping("/api")
public class TestApiController {

    @GetMapping("/test")
    public String test(){
        return "hello";
    }

    @GetMapping("/test1")
    public String giveMe(@RequestParam String hello){
        return "who";
    }

    @PostMapping("/test")
    public String test(@RequestParam String hello){
        return "post";
    }
}
```

6. ### 결과확인 
```java
실행하여 제대로 API 문서가 생성되었는지 확인한다.

Swgger URL: http://<ip>:<port>/<base>/swagger-ui.html
예: http://localhost:8080/swagger-ui.html

```

7. ### 자주쓰는 Annotation
```java
* RestController 클래스에 사용
 - @Api(value = "SwaggerTestController")

* RestController 클래스안 메소드에 사용
 - @ApiOperation(value = "test", notes = "테스트입니다.")

* 메소드 파라미터에 사용
 - @ApiParam(value = "게시판번호", required = true, example = "1")
```

8. ### springsecurity에서 구현한 login/logout Swagger로 api 표시하기
- [참고](https://stackoverflow.com/questions/34386337/documenting-springs-login-logout-api-in-swagger)
```java
 * 실제 동작하는 코드가 아닌 API용도
 * spring security filter가 가로채서 처리 하므로 실행되지 않는다.

@Api(tags = "로그인 & 로그아웃")
@RequestMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuthApi {
    @ApiOperation("로그인")
    @PostMapping("/login")
    public void fakeLogin(@ApiParam("email") @RequestParam String email, @ApiParam("Password") @RequestParam String password) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("로그아웃")
    @PostMapping("/logout")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }
}
```

- ### 참고
	 - [Access Denied 참고](https://stackoverflow.com/questions/54458324/o-s-boot-springapplication-application-run-failed)
     - [API 설명 블로그 참고](https://dev-jwblog.tistory.com/20)
     - [swagger docs](https://docs.swagger.io/swagger-core/v1.5.0/apidocs/io/swagger/annotations/package-summary.html)
