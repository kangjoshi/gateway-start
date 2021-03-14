### Spring Cloud Gateway
- API로 라우팅하는 간단하면서도 효과적인 방법을 제공
- 보안, 모니터링, 탄력적인 운영과 같은 부가적인 필터를 제공할 수 있다.

#### 동작
1. 클라이언트는 Spring Cloud Gateway 서버에 요청
2. 서버는 요청이 지정된 매핑 경로와 일치한다고 판단하면 핸들러로 전송 핸들러는 필터 체인을 통해 요청을 실행
3. 필터는 프록시 요청이 전송되기 전과 후 로직을 실행할 수 있다. 즉 사전필터 수행 -> 프록시 요청 -> 사후필터 수행 동작


#### Route Predicate 구성
1. 쿠키
    ```yaml
      spring:
        cloud:
          gateway:
            routes: 
              - id: cookie_route
                url: https://example.org
                predicates:
                  - Cookie=chocolate, ch.p   # chocolate의 쿠키 값이 ch.p 정규식과 일치하는지
    ```
2. 헤더
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: header_route
                url: https://example.org
                predicates:
                  - Header=X_Request_Id, \d+ # Request Header X_Request_Id에 \d+(하나 이상의 숫자 값이 있음) 정규식과 일치하는지
    ```
3. host
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: host_route
                url: https://example.org
                predicates:
                  - Host=**.somehost.org # Request Header Host 값이 **.somehost.org와 일치하는지
    ```          
3. http method
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: method_route
                url: https://example.org
                predicates:
                  - Method=GET,POST 
    ```
4. path
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: path_route
                url: https://example.org
                predicates:
                  - Path=/red/{segment},/blue/{sagment}
    ```

#### GatewayFilter
- Http 요청 또는 응답을 수정할 수 있는 filter
1. AddRequest(Response)HeaderGatewayFilter
    - Request(Response) Header에 지정한 Header 추가
    ```yaml
    spring:
      cloud:
         gateway:
           routes:
           - id: add_request_header_route
             uri: https://example.org
             filters:
             - AddRequestHeader=X-Request-color, blue
    ```
2. AddRequest(Response)ParameterGatewayFilter
   - Request(Response) parameter에 지정한 parameter 추가
   ```yaml
   spring:
     cloud:
        gateway:
          routes:
          - id: add_request_parameter_route
            uri: https://example.org
            filters:
            - AddRequestParameter=color, blue
   ```
3. DedupeResponseHeaderGateway
    - Response Header에서 중복된 값을 제거한다.
    - 선택적으로 제거 전략을 설정할 수 있다.
        1. RETAIN_FIRST
        2. RETAIN_LAST
        3. RETAIN_UNIQUE
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: dedupe_response_header_route
            uri: https://example.org
            filters:
            - DedupeResponseHeader=Access-Control-Allow-Credentials Access-Control-Allow-Origin  # Access-Control-Allow-Credentials 와 Access-Control-Allow-Origin의 중복 제거
    ```
4. MapRequestHeaderGatewayFilter
    - `fromHeader`에 지정한 헤더와 동일한 값을 가지는 `toHeader`를 생성한다.
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: map_request_header_test
              uri: ${test.uri}
              predicates:
                - Path=/headers
              filters:
                - MapRequestHeader=X-Request-Blue, X-Request-Red # X-Request-Blue와 같은 값을 가지는 X-Request-Red 헤더를 만든다.
    ```
   - Map~으로 시작하여 헤더의 이름이 변경 되겠지 생각 했지만 그냥 같은 값을 가지는 헤더를 하나 더 만드는 것
5. PrefixPathGatewayFilter
   - 요청에 대해 prefix를 지정한다.
   ```yaml
       spring:
         cloud:
           gateway:
             routes:
               - id: prefix_path_test
                 uri: ${test.uri}
                 filters:
                   - PrefixPath=/mypath  # 모든 요청에 대해서 /mypath를 붙인다 (/hello -> /mypath/hello)
   ```
6. RedirectToGatewayFilter
    - 3XX Redirect 응답을 하는 필터
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: redirect_test
            uri: ${test.uri}
            filters:
            - RedirectTo=302, https://spring.io  # 302 Location:https://spring.io Redirection이 된다.
    ```
7. RemoveRequestHeaderGatewayFilter (or RemoveResponseHeaderGatewayFilter)
    - 제거할 Header를 지정한다.
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
            - id: remove_request_header_test
              uri: ${test.uri}
              filters:
                - RemoveRequestHeader=X-Request-Foo
    ```
8. RemoveRequestParameterGatewayFilter
    - 제거할 Parameter를 지정한다.
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: remove_request_parameter_test
            uri: https://example.org
            filters:
            - RemoveRequestParameter=red
    ```
9. RewritePathGatewayFilter
    - 정규식을 이용하여 uri path를 rewrite한다.
    ```yaml
    spring:
      cloud:
        gateway:
          routes:
          - id: rewritepath_test
            uri: https://example.org
            predicates:
            - Path=/red/**
            filters:
            - RewritePath=/red(?<segment>/?.*), $\{segment}  # /red/blue -> /bule
    ```
  
   
Hystrix 관련 필터들..은 나중에...   
   
   