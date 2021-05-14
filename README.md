# java-was
java Was 

### HTTP/1.1 WebApplicationServer
##Config 목록
대상 config : http-conf.json
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
virtualServers : 호스트 서버를 추가한다. (해당부분은 여러대의 포트를 생성할수있다.)
virtualServers.port : was가 생성되는 포트
virtualHosts : 가상서버 호스트의 입력값 (해당 도메인과 documentRoot 등 스펙을 여러대 가능하다.)
virtualHosts.serverName : 도메인 서버호스트
virtualHosts.documentRoot : 도메인 서버 Root 경로
virtualHosts.errorDocument_403 : 403 페이지 html
virtualHosts.errorDocument_404 : 404 페이지 html
virtualHosts.errorDocument_500 : 500 페이지 html

##구현 순서
1. WebApplicationServer 생성하여 Server.start() 진행
2. 위 해당 config을 읽어 WebApplicationServer 생성
3. Was 서버의 포트를 생성되었으며 클라이언트 응답을 대기한다.
~~~
[INFO ](com.ddoriya.was.server.HttpContainer:43) Accepting connections on port : 8081
[INFO ](com.ddoriya.was.server.HttpContainer:43) Accepting connections on port : 8082
~~~

##매핑 URL


