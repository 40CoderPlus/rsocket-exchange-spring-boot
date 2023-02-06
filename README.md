# RSocket Exchange Auto Create Proxy Service

Add annotation `@RSocketClient` for `@RScoketExchange`.

Add annotation `@EnableRSocketClients` for init beans with annotation `@RSocketClient`.

# Requirements

- Spring Framework 6/Spring Boot 3
- Java 17+

# How to use

Annotation interface by `@RSocketClient`

```java
@RSocketClient
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

Use `AnswerService` to communicate with RSocket Server

```java
@AllArgsConstructor
@RestController
public class QuestionApi {

    private AnswerService answerService;

    @RequestMapping(path = "/question")
    public Mono<Answer> question(@RequestParam(name = "current") int current) {
        return answerService.answer(new Question(current));
    }
}
```

More about `@RSocketClient`

Use you own `RSocketServiceProxyFactory`
```java
@RSocketClient(proxyFactory = "myProxyFactory")
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

Use you own `RSocketRequester`
```java
@RSocketClient(rsocketRequester = "myRSocketRequester")
public interface AnswerService {

    @RSocketExchange("answer")
    Mono<Answer> answer(@Payload Question question);
}
```

See more in module [rsocket-exchange-spring-boot-sample](rsocket-exchange-spring-boot-sample)