# WebApplicationServer
> 이상준

# 주요기능
- HTTP/1.1 WebApplicationServer 서버 생성
- SimpleServlet 따른 Service 생성

##스펙
1. HTTP/1.1 의 Host 헤더를 해석
   * 해당 헤더를 분석하여 HttpRequest 관리
    

2. 설정 파일로 관리
   * http-conf.json 파일 관리
    

3. 403, 404, 500 오류를 처리
   * conf 파일을 통해 코드별 오류 처리
    

4. 다음과 같은 보안 규칙
* 다음 규칙에 걸리면 응답 코드 403 을 반환합니다
* HTTP_ROOT 디렉터리의 상위 디렉터리에 접근할 때
* exe 확장자가 접근시


5. logback 프레임워크 http://logback.qos.ch/를 이용하여 다음의 로깅 작업
   * log 로깅 관리 진행
    

6. 간단한 WAS 를 구현
   * SimpleServlet 구현체 구현
   * Hello, service.Hello 클래스 구현
    

7. 현재 시각을 출력하는 SimpleServlet 구현체
   * DateSimpleService 클래스 구현
    

8. 앞에서 구현한 여러 스펙을 검증하는 테스트 케이스 구현
   * test 경로에 케이스 구현
    

##Config 목록
> 대상 config : http-conf.json
~~~
{
  "virtualServers": [
    {
      "port": "8081",
      "virtualHosts": [
        {
          "serverName": "test1.com",
          "documentRoot": "C:/home1/was/test1",
          "errorDocument_403": "403.html",
          "errorDocument_404": "404.html",
          "errorDocument_500": "500.html"
        },
        {
          "serverName": "test2.com",
          "documentRoot": "C:/home1/was/test2",
          "errorDocument_403": "403.html",
          "errorDocument_404": "404.html",
          "errorDocument_500": "500.html"
        }
      ]
    }
  ]
}
~~~
* virtualServers : 호스트 서버를 추가한다. (해당부분은 여러대의 포트를 생성할수있다.)
* virtualServers.port : was가 생성되는 포트
* virtualHosts : 가상서버 호스트의 입력값 (해당 도메인과 documentRoot 등 스펙을 여러대 가능하다.)
* virtualHosts.serverName : 도메인 서버호스트
* virtualHosts.documentRoot : 도메인 서버 Root 경로
* virtualHosts.errorDocument_403 : 403 페이지 html
* virtualHosts.errorDocument_404 : 404 페이지 html
* virtualHosts.errorDocument_500 : 500 페이지 html

##서버 생성 순서
*  WebApplicationServer 생성하여 Server.start() 진행
*  위 해당 config을 읽어 WebApplicationServer 생성
*  Was 서버의 포트를 생성되었으며 클라이언트 응답을 대기한다.
~~~
[INFO ](com.ddoriya.was.server.HttpContainer:43) Accepting connections on port : 8081
[INFO ](com.ddoriya.was.server.HttpContainer:43) Accepting connections on port : 8082
~~~

##매핑 URL
* URL , Class 위치
~~~
/Hello", "Hello"
/service.Hello", "service.Hello"
/date", "service.DateSimpleService"
~~~

## 기능 목록
* http-config 내용을 읽는다.
* http 서버를 생성한다.
* documentRoot 의 경로로 진행한다.
* /Hello 요청시 name의 parameter값을 받아 화면에 입력한다.
* /service.Hello 요청시 name의 parameter값을 받아 화면에 입력한다.
* /date 요청시 현재 시간을 출력한다.
* was 서버의 / 요청시 index.html 를 자동 매핑되어 생성된다.
* Error 요청에 따라 403, 404, 500 구분하여 출력된다. 
* was서버를 사용시 SimpleServlet 상속하여 Service 를 구현한다.