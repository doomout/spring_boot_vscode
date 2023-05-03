도서명 - 자바 웹 개발 워크북  
IDE -  vsCode로 변경  
자바 버전 - JDK 11, JAVA EE 8  
웹 서버 - 자체 내장 WAS   
DB - MariaDB 10.5(x64)  
SQL 툴 - HeidiSQL 11.3.0.6295  
스프링 프레임워크 버전 - 5.3.19  
부트스트랩 버전 - 5.1.3  

1. application.properties 설정
```
//DB 관련
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://localhost:3306/webdb
spring.datasource.username=doom
spring.datasource.password=ska123
server.port=8081

//Log4j2 관련
logging.level.org.springframework=info
logging.level.org.zerock=debug

//JPA 관련
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.show-sql=true
```
2. build.gradle 설정
```
//Querdsl 설정
buildscript {
	ext {
		queryDslVersion = "5.0.0"
	}
}
dependencies {
	implementation 'org.springframework.boot:spring-boot-starter-data-jpa'
	implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'
	implementation 'org.springframework.boot:spring-boot-starter-web'
	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
	annotationProcessor 'org.projectlombok:lombok'
	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testCompileOnly 'org.projectlombok:lombok'
	testAnnotationProcessor 'org.projectlombok:lombok'
	implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0'

    implementation 'org.modelmapper:modelmapper:3.1.0'
	
	//Querdsl 설정
	implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
	annotationProcessor(
			"javax.persistence:javax.persistence-api",
			"javax.annotation:javax.annotation-api",
			"com.querydsl:querydsl-apt:${queryDslVersion}:jpa"
	)
}
//Querdsl 설정
sourceSets{
	main {
		java {
			srcDirs = ["$projectDir/src/main/java", "$projectDir/build/generated"]
		}
	}
}
```
3. JSON 데이터 란?
  * JavaScript Object Notation 의 약자로 구조를 가진 데이터를 자바스크립트의 객체 표시법으로 표현한 순수 문자열 
  * 순수 문자열이기에 데이터 교환시에 프로그램 언어에 독립적이다.
  * 스프링은 jackson-databind 라는 별도의 라이브러리를 추가 후 개발해야 한다.
  * 스프링 부트는 web 항목 추가할 때 자동으로 포함되어 별도의 설정 없이 바로 개발 가능하다.
  * 화면은 Thymeleaf를 이용하고 데이터는 json으로 전송하는 방식으로 개발한다.

4. Thymeleaf 문법
```html
<!--/* 주석 처리 + 에러 찾기 좋은 형태 주석 */-->
<!--/*변수 선언법*/-->
<div th:with="num1=${10}, num2=${20}">
    <h4 th:text="${num1 + num2}"></h4> <!--/*결과 : 30 출력*/-->
</div>
<!--/*반복문 2가지 형태*/-->
<ul>
    <li th:each="str: ${list}" th:text="${str}"></li>
</ul>
<ul>
    <th:block th:each="str: ${list}">
        <li>[[${str}]]</li>
    </th:block>
</ul>
<!--/*반복문의 status 변수 (index/count/size/first/last/odd/even 등 사용)*/-->
<ul>
    <li th:each="str,status: ${list}">
        [[${status.index}]] -- [[${str}]]
    </li>
</ul>
<!--if/unless 문-->
<ul>
    <li th:each="str, status: ${#list}">
        <span th:if="${status.odd}">ODD -- [[${str}]]</span>
        <span th:unless="${status.odd}">EVEN -- [[${str}]]</span>
    </li>
</ul>
<!--이항 연산자-->
<ul>
    <li th:each="str,status:${list}">
        <span th:text="${status.odd}?'ODD ---' + ${str}"></span>
    </li>
</ul>
<!--삼항 연산자-->
<ul>
    <li th:each="str,status: ${list}">
        <span th:text="${status.odd} ? 'ODD ---' + ${str} : 'EVEN ---' +${str}"></span>
    </li>
</ul>
<!--switch / case 문-->
<ul>
    <li th:each="str,status: ${list}">
        <th:block th:switch="${status.index % 3}">
            <span th:case="0">0</span>
            <span th:case="1">1</span>
            <span th:case="2">2</span>
        </th:block>
    </li>
</ul>
<!--링크 처리들-->
<a th:href="@{/hello}">Go to /hello</a>
<a th:href="@{/hello(name='AAA', age=16)}">Go to /hello</a>
<a th:href="@{/hello(name='한글처리', age=16)}">Go to /hello</a>
<a th:href="@{/hello(types=${{'AAA','BB','CC'})}">Go to /hello</a>
```
5. JPA 란?
    * 데이터에 해당하는 객체를 엔티티객체라는 것으로 다루고 JPA로 DB와 연동해서 관리
    * 엔티티 객체 - PK(기본키)를 가지는 자바의 객체. @Id 를 이용해서 객체를 구분
    * 엔티티 클래스는 반드시 @Entity 가 존재하고 엔티티 객체 구분을 위한 @Id가 필요
    * @MappedSuperClass : 공통으로 사용되는 칼럼들 지정하고, 해당 클래스를 상속하여 사용
    * AuditingEntityListener : 엔티티가 DB에 추가,변경시 자동으로 시간 값을 지정
    * @EnableJpaAuditing : AuditingEntityListener 를 활성하기 위해선 지정
    * JpaRepository<엔티티 타입, @Id 타입> : 인터페이스를 선언하는 것만으로 DB 관련 작업 처리 가능
    * save() : insert/update 를 실행하는 기능 
    * findById() : 조회 기능, 리턴 타입은 Optional<T>
    * deleteById() : 삭제 기능
    * Pageable, page<E> : 페이징 처리 
    * @Query : SQL 과 유사하게 JPA에서 사용하는 쿼리 언어
    * 원하는 속성만 추출해서 Object[] 로 처리 하거나 DTO로 처리하는 기능
   