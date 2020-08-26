
 # Spring-Web-MVC
## 스프링 MVC 핵심 기술 활용

---
1. 인텔리제이 - SpringBoot, Thymeleaf 프로젝트생성
2. Junit4를 사용하기 위해 pom.xml 의존성 수정 [exclusions 주석처리](https://ratseno.tistory.com/75)
3. http 요청 맵핑 (연습문제완료X)
4. Handler Method
	1.URI 패턴 `GET`
		- @PathVariable
		- @MatrixVariable
	2.요청매개변수`GET, POST` 
		- @RequestParam
		- @ModelAttribute + [@Vaildated , BindingResult]