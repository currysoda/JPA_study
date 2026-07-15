JPA 학습용 디렉토리

---

ex1-hello-jpa-start - 자바 ORM 표준 JPA 프로그래밍 기본편

옛날 자료라서 maven pom.xml 사용했지만

gradle application.yml 사용으로 변경함

logback.xml -> log4j 사용하고 application.yml 에 설정 통합

persistence.xml 은 필수 -> spring 없이 JPA 만 사용하려면

entity 자동 스캔 스프링 없이 하이버네이트만 사용시 제대로 작동 안함

적용법 찾아내서 적용했지만 그닥 권장되는 방법 아님 그러나 보통 스프링 없이 쓸 일이 없으므로 괜찮을지도?

---

실전! 스프링 부트와 JPA 활용1 - 웹 애플리케이션 개발
실전! 스프링 부트와 JPA 활용2 - API 개발과 성능 최적화
