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
    implementation 'org.springframework.boot:spring-boot-starter-validation'
    //스프링 시큐리티 적용
    implementation 'org.springframework.boot:spring-boot-starter-security'
    //Swagger UI 설정
    implementation 'io.springfox:springfox-boot-starter:3.0.0'
	implementation 'io.springfox:springfox-swagger-ui:3.0.0'
    //이미지 업로드시 작은 이미지로 생성함
    implementation group: 'net.coobird', name: 'thumbnailator', version: '0.4.16'
    implementation 'nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.1.0'
    implementation 'org.modelmapper:modelmapper:3.1.0'
    //Querdsl 설정
	implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"

	compileOnly 'org.projectlombok:lombok'
	developmentOnly 'org.springframework.boot:spring-boot-devtools'
	runtimeOnly 'org.mariadb.jdbc:mariadb-java-client'
	annotationProcessor 'org.projectlombok:lombok'

	testImplementation 'org.springframework.boot:spring-boot-starter-test'
	testCompileOnly 'org.projectlombok:lombok'
	testAnnotationProcessor 'org.projectlombok:lombok'
	
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
6. vsCode 에서 java 여러 버전 사용법 
    * vsCode 확장팩 Extension Pack for Java가 기본 자바 11 이상을 지원한다.
    * JDK가 11 이하가 설치 되어 있는 경우 실행시 오류가 발생한다.
    * 설정에서 java:home 으로 검색 후 "settings.json 에서 편집" 클릭 하단 코드를 추가 
```json
 "java.configuration.runtimes": [
        {
            "name": "JavaSE-1.8", //자바 버전
            "path": "C:\\Program Files\\Java\\jdk1.8.0_202" //설치 경로
        },
        {
            "name": "JavaSE-11", //자바 버전
            "path": "C:\\Program Files\\Java\\jdk-11", //설치 경로
            "default": true //기본으로 이 버전을 사용하겠다는 것
        }
    ],
```     
7. MariaDB 한글 설정법
    * 윈도우 : 설치 경로\data\my.ini 파일 열기
    * 리눅스 : /etc/my.cnf.d 파일 열기 
    * 하단 설정 입력 후 mariaDB 재기동
    * SHOW VARIABLES LIKE 'c%' 으로 확인
```text
[client]
default-character-set=utf8

[mysqld]
character-set-server=utf8
collation-server=utf8_general_ci
init_connect=SET collation_connection=utf8_general_ci
init_connect=SET NAMES utf8

[mysql]
default-character-set=utf8
```
8. 비동기 처리와 Axios 
    * 비동기 처리란? 여러 작업을 동시에 처리 하기 위해 각 작업에서 결과가 나오면 해당하는 작업에게 "통보"를 해주는 방식으로 작동하는 처리 
    * 통보하는 과정을 콜백(callback) 라고 한다.
    * Axios 란? Ajax를 호출하는 코드를 동기화된 방식처럼 작성할 수 있게 해준다.
    * 비동기 호출법
```js
    <!--Axios 사용하기 위한 라이브러리 설정(read.html에 설정)-->
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <!--/js/reply.js 를 Axios 방식으로 처리하겠다는 것-->
    <script src="/js/reply.js"></script>
``` 
```js
//async 는 해당 함수가 비동기 처리를 위한 함수라는 것을 명시
async function get1(bno) {
    const result = await axios.get(`/replies/list/${bno}`) //await 은 비동기 호출하는 부분을 명시

    console.log(result)
}
```
9. 프로젝트 전체적인 구조
    * DTO : 데이터 전송을 위한 객체로서, 데이터를 담아서 전송하거나 받을 때 사용
    * Repository : 데이터베이스와 관련된 작업을 처리하는 인터페이스
    * Service : 비즈니스 로직을 처리하는 인터페이스
    * Domain : 비즈니스 도메인을 나타내는 객체로서, 데이터베이스와의 매핑을 위한 클래스
    * Controller : 웹 요청에 대한 처리를 담당하는 클래스
    * Config : 프로젝트 설정
    * Test : 함수 별로 테스트 코드 작성, 검증
    * Resources : html, css, javaScript, image 파일 같은 화면단 모음

10. vsCode 사용시 주의점(이것 때문에 시간 엄청 보넀음 ㅡㅡ)
    * 확장에 Language Support for Java(TM) by Red Hat 가 강제로 설치 되고 삭제도 안되게 바뀌었다.
    * Language Support for Java(TM) by Red Hat 는 Eclipse Adoptium 자바 sdk를 쓰게 강제한다.
    * 기존 오라클 sdk가 설정으로 되어 있으면 읽지 못한다. 
    * 최소 11 버전 이상을 쓰라고 강제한다.
    * 자바 여러 버전을 사용할 수 있게 해주긴 한다... 