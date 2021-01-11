## Spring boot 운영
---
1. 스프링부트는 Actuator라는 모듈 제공
	- 스프링부트 운영중에 주시할 수 있는 유용한 정보들 제공
	- 정보들을 EndPoint를 통해 제공해준다.
	```java
	* EndPoint : 통신의 끝점
	```
	![image](https://user-images.githubusercontent.com/60174144/104096264-cc601b80-52de-11eb-8e75-f49af0715ea2.png)
	- EndPoint를 통해 확인할 수 있는 정보 흭득하기 
		1. Spring-boot-starter-actuator 의존성 추가
		2. [해당 문서](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#production-ready-endpoints)에서 나온 모든 ID에 해당하는 Actuator들 활성화 된다.
		```java
		* auditevents : 누가 인증에 성공하고 실패 했는지에 대한 정보
		* beans : 컨테이너에 등록된 빈들 정보
		* conditions : 어떤 자동설정이 어떤 조건에 의해 설정되고 설정안되었는지 정보
		* configprops : application.properties에 정의가 가능한 것들 
		* httptrace : 최근 100개의 http 요청과 응답 정보
		* logger : 패키지의 로깅레벨
		* matrics : application의 핵심 정보 ( app이 사용하는 메모리, cpu 등)
		* mappings : controller 매핑 정보
		* shotdown (유일하게 비활성화 되어있다.) 
		등등
		```	
2. Spring-boot-admin
	- [참고](https://jaehyun8719.github.io/2019/06/20/springboot/admin/)
	
