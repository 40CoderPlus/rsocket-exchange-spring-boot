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

### Limit

Current can't auto register `@RSocketClient` bean for Server push message to client.
But we can do like this:

```groovy
AnswerService createServiceForClient(RSocketRequester rsocketRequester) {
    return RSocketServiceProxyFactory.builder(rsocketRequester).build()
        .createClient(AnswerService.class);
}
```

See more:
- [rsocket-exchange-spring-boot-sample](rsocket-exchange-spring-boot-sample)
- [Spring RSocket](https://docs.spring.io/spring-framework/docs/current/reference/html/rsocket.html)
- [RSocket](https://rsocket.io)