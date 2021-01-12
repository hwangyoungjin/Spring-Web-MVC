# Spring-Web
## 스프링, 스프링부트 내용정리
---

###목차
---
1. [Spring 개념](https://github.com/hwangyoungjin/Spring-Web-MVC#Spring-개념)

2. [Spring-boot 구조 및 원리](https://github.com/hwangyoungjin/Spring-Web-MVC#Spring-boot-구조-및-원리)

3. [Spring-boot-mvc](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-boot-web-mvc)

4. [Spring-boot-data](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-boot-data)

5. [Spring-boot-Security](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-boot-security)

6. [Spring-boot-RestClient](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-boot-RestClient)

7. [Spring-boot-운영](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-boot-production)

8. [Spring-study](https://github.com/hwangyoungjin/Spring-Web-MVC/tree/master/spring-study)

#Spring 개념
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


## Spring-boot 구조 및 원리
---
1. 스프링부트 구조
2. 스프링부트의 원리
	1. 의존성 관리
	2. 자동설정 원리
	3. 내장 웹 서버 이해/응용(1부-컨테이너와 서버포트/2부- http1.1 , http2 , https)
	4. 독립적으로 실행 가능한 jar