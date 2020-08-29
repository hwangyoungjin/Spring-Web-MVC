# Spring-Web-MVC
## 스프링 MVC 핵심 기술 활용

---
1. 인텔리제이 - SpringBoot, Thymeleaf 프로젝트생성

2. Junit4를 사용하기 위해 pom.xml 의존성 수정 [exclusions 주석처리](https://ratseno.tistory.com/75)

3. http 요청 맵핑 (연습문제완료X)

4. Handler Method
    1. URI 패턴 `GET`
     - @PathVariable
     - @MatrixVariable
     
    2. 요청매개변수 `GET, POST` 
     - @RequestParam
     - @ModelAttribute + [@Vaildated , BindingResult]
     - └> **@valid적용안됨 [dependency추가로 해결](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/2.3.3.RELEASE)**
     - 바인딩 에러 발생시 에러표시
     - POST 후 브라우져 새로고침하는 경우 Redirect이용 : POST-> Redirect -> GET

    3. 모델정보 HttpSession에 저장
     - @SessionAttributes으로 모델정보 Session에 등록
     - └> 파라미터에 @ModelAttribute으로 모델 받아오기
     - └> 파라미터 SessionStatus 으로 세션 종료 	

