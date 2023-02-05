# RSocket Exchange Auto Create Proxy Service

Add annotation `@RSocketClient` for `@RScoketExchange`.

Add annotation `@EnableRSocketClients` for init beans with annotation `@RSocketClient`.

# How to use

```java
@RSocketClient
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

or
```java
@RSocketClient(proxyFactory = "myProxyFactory")
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

or
```java
@RSocketClient(rsocketRequester = "myRSocketRequester")
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

See more in module [rsocket-exchange-spring-boot-sample](rsocket-exchange-spring-boot-sample)