# telegram-framework
- 어너테이션 기반의 확장 가능한 텔레그램 채팅 봇 프레임워크
- main 파티션과 application 파티션의 분리하여 사용성을 높이고 비니지스 로직에 집중할 수 있도록 각종 기능 지원

# 사용
- telegram-bot과 telegram-framework을 의존성에 추가
- spring-context를 기반으로 작성되었으며, 편의를 위하여 spring-boot을 부모로 하여 프로젝트 생성을 권장

```groovy
dependencies {
    implementation 'io.github.infoqoch:telegram-framework:0.4.3'
    implementation 'io.github.infoqoch:telegram-bot:0.2.4'
}
```

# 기본적인 사용
- simple-in-spring 프로젝트 참고 (spring-boot 2.7.5 기준)

## 1. @SpringBootApplication 
- @SpringBootApplication 위치에 아래의 어너테이션을 삽입합니다.
- @EnableTelegramFramework
- @EnableScheduling
- @EnableAsync

```java
import infoqoch.telegram.framework.update.EnableTelegramFramework;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableTelegramFramework
@EnableScheduling
@EnableAsync

@SpringBootApplication
public class SimpleInSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(SimpleInSpringApplication.class, args);
    }

}
```

## 2. telegram-framework.properties
- telegram-framework.properties 를 다음의 양식으로 작성합니다.
- 구체적인 텔레그렘 봇 api 및 토큰 생성은 다음 링크를 참고하여 주십시오.
- https://core.telegram.org/bots#how-do-i-create-a-bot

```properties
telegram.token= # 반드시 입력
telegram.file-upload-path= # 생략 가능
telegram.send-message-after-update-resolved = true
```

## 3. controller
- controller를 작성합니다.

```java
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.springframework.stereotype.Component;

@Component
public class BaseController {
    // "*" 은 반드시 구현해야 한다. 어떤 명령어에도 해당하지 않는 채팅메시지에 대응한다.
    @UpdateRequestMapper({"*", "help"})
    public MarkdownStringBuilder help(){
        return new MarkdownStringBuilder()
                .plain("안녕하세요! 현재 제공하는 기능은 다음과 같습니다.").lineSeparator()
                .command("help", null).plain(" -> 도움말").lineSeparator()
                .command("echo", "echo").plain(" -> 입력한 값을 메아리로 돌려준다").lineSeparator()
                .plain("명령을 할 때 슬러시('/')는 생략 가능하며 언더바('_')는 띄어쓰기(' ')로 대체합니다.");
    }

    @UpdateRequestMapper({"echo", "에코", "애코", "메아리", "야호"})
    public MarkdownStringBuilder echo(UpdateRequestCommandAndValue commandAndValue){
        return new MarkdownStringBuilder().italic(commandAndValue.getValue()+"~~~");
    }
}
```

# 구체적인 사용
- 프로젝트 sample-in-spring 을 참고하여 주십시오.

## UpdateDispatcher와 채팅 메시지 분석기
- telegram-framework의 가장 핵심적인 기능으로 frontend controller pattern 을 기반으로 작성되었습니다.
- UpdateDispatcher는 명령어가 명시된 @UpdateRequestMapper을 기반으로 동작합니다. 위의 예제 중 @UpdateRequestMapper("echo") 는 "/echo_"로 시작하는 클라이언트의 메시지를 분석하기 위한 핸들러입니다.
- @UpdateRequestMapper로 선언된 핸들러의 시그니처는 필요에 따라 적절한 파라미터 타입과 리턴 타입을 가질 수 있습니다. UpdateRequestParam와 UpdateRequestReturn를 구현한 타입이 그것이며, 이는 adapater pattern을 통해 구현되었습니다.

## UpdateDispatcher의 관심사
- UpdateDispacher는 클라이언트의 채팅 메시지를 분석하는 것을 관심사로 가집니다. 만약 채팅에 대한 단순한 분석기로 사용할 경우 `@EnableTelegramFramework`를 `@Configuration`에 선언하여 사용한 후 UpdateDispacher 빈을 주입하여 사용할 수 있습니다. 리턴 타입은 더는 특별한 의미를 가지지 않으므로 void로 선언합니다.
- 구체적인 사용은 방식은 `infoqoch.telegram.framework.update.dispatcher` 패키지의 테스트 코드를 참고 바랍니다.

## UpdateRunner와 실시간 클라이언트의 채팅 수집 및 응답을 위한 스케줄러
- UpdateDispatcher가 클라이언트의 응답 메시지를 분석하기 위하여 사용한다면, UpdateRunner는 UpdateDispatcher를 쉽게 다루기 위하여 사용합니다. UpdateRunner는 클라이언트의 채팅 메시지인 Update를 pooling으로 수집하고, 이를 UpdateDispatcher로 해석 및 결과값을 생산한 후, Send 객체로 TelegramBot을 통해 클라이언트에 응답합니다.
- UpdateRunner는 스프링 스케줄러를 기반으로 작성되었습니다. UpdateRunner는 Send를 이벤트퍼블리셔(ApplicationEventPublisher)로 SendUpdateResponseEventListener에 전달하여 처리합니다.
- 스케줄러 및 이벤트 퍼블리셔의 정상적인 처리를 위하여 설정빈에 다음의 어너테이션을 선언합니다. `@EnableScheduling`, `@EnableAsync`. 
- 프로퍼티의 `telegram-framework.properties` 를 다음과 같이 설정하여 SendUpdateResponseEventListener를 빈으로 등록합니다. `telegram.send-message-after-update-resolved = true`

## UpdateRequestParam과 UpdateRequestReturn
- @UpdateRequestMapper의 핸들러는 UpdateRequestParam와 UpdateRequestReturn의 구현체를 파라미터와 리턴 타입으로 사용할 수 있습니다. 
- 기본적으로 구현된 파라미터 타입과 리턴 타입은 다음과 같습니다.
- 파라미터 타입
  - UpdateMessage : 클라이언트의 단순 메시지
  - UpdateDocument : 클라이언트의 파일 타입 메시지 (엑셀, 텍스트 등)
  - UpdateRequestCommandAndValue : UpdateDispacher의 분석결과로 도출된 명령어(command)와 값(value)
  - UpdateRequest : 텔레그렘 서버에서 받은 데이터 전체 값. 어텝터 패턴의 기준이 되는 데이터 타입 (서블릿의 HttpServletRequest 에 대응)
- 리턴 타입 (특히 UpdateRunner를 사용할 경우)
  - void : 메세지를 응답하지 아니함.
  - String : 단순 문자
  - MarkdownStringBuilder : 마크다운
  - UpdateResponse : 텔레그렘 서버에 응답을 위한 데이터 타입. 어텝터 패턴의 기준이 되는 데이터 타입 (서블릿의 HttpServletResponse 에 대응)

### UpdateRequestParam와 UpdateRequestReturn의 확장
- CustomUpdateRequestParamRegister, CustomUpdateRequestReturnRegister 를 구현하여 파라미터와 리턴 타입을 확장 가능합니다. 
- 구체적인 내용은 sample-in-spring 프로젝트의 `infoqoch.telegramframework.spring.sampleinspring.config` 패키지를 참고하여 주시기 바랍니다.

## SendUpdateResponseEventListener의 구체적인 활용과 비동기 처리의 필요성
- SendUpdateResponseEventListener는 Send 타입을 처리합니다. 
- Send는 텔레그렘과의 통신이 완료되었는지를 확인하기 위한 메서드 isDone이 있으며, 이는 CompletableFuture으로 작성되었습니다.
- 만약 로깅 등 어떤 이유로 Send를 처리하는 리스너를 구현해야 한다면 **반드시 isDone으로 종료를 확인해야 하며, 비동기@Async로 처리해야 합니다**. 
- sample-in-spring 프로젝트의 `infoqoch.telegramframework.spring.sampleinspring.log` 패키지를 확인하여 주시기 바랍니다.
- 아래는 로깅을 위한 리스너의 예시입니다.

```java
public class SomeListener{
  @Async
  @EventListener(Send.class)
  public void handle(Send send) {
    while(!send.isDone());

    UpdateLog updateLog = ifUpdateLogExistThenSave(send);

    SendLog sendLog = SendLog.of(send, updateLog);
    sendLogRepository.save(sendLog);
  }
}
```

# 아키텍쳐
## SRP
- 스프링의 DispacherServlet과 같이 사용성 보장과 비지니스 로직에 집중하기 위한 프레임워크를 목표로 제작되었습니다.
- 결합도를 최대한 낮추고 각 모듈마다의 단일 책임을 지키기 위하여 개발되었습니다.
- telegram-bot : 텔레그렘과의 통신(자바 기반 구현)
- telegram-framework : main 파티션의 분리
  - UpdateDispatcher : 클라이언트의 채팅 메시지 분석과 확장 가능성
  - UpdateRunner : 실시간 처리를 위한 스케줄러
  - SendUpdateResponseEventListener : 분석된 값을 사용자에게 전달. 이벤트 기반 동작

## TDD와 OOP 지향
- 테스트 주도로 개발하였습니다. 
- 가능한 작은 기능으로 분리하여 테스트 및 재사용 가능하도록 구현하였습니다.
- 유닛 테스트와 통합 테스트를 분리하였습니다.

## main 파티션과 application 파티션의 분리
- main 파티션을 담당하는 프레임워크로 완전하게 분리시켰습니다.
- @UpdateRequestMapper를 기반으로 비지니스 로직에 집중할 수 있습니다. 
- 스프링 컨텍스트의 로딩 시점에서 UpdateDispatcher 등 기능의 유효성을 검사합니다. 런타임 시점에서 에러가 발생하지 않도록 구현하였습니다.

# 주요 구현 프로젝트
- 현재 프로젝트의 simple-in-spring 프로젝트와 sample-in-spring 프로젝트 이외
 
## telegram-bot
- [텔레그램 봇 telegram-bot](https://github.com/infoqoch/telegram-bot)
- https://github.com/infoqoch/telegram-bot
- telegram bot과의 통신을 위한 라이브러리

## dictionary-bot
- [사전봇 dictionary-bot](https://github.com/infoqoch/dictionary-v3)
- https://github.com/infoqoch/dictionary-v3
- telegram-framework를 기반으로 구현한 사전봇
- 현 프로젝트가 탄생하기 된 계기
- 현재 텔레그램의 채널로 동작 중 : @custom_dictionary_bot

# 프로젝트 로그
- v1. 2021.07 - 2021.12
  - dictionary-bot 기반의 프로젝트
  - 동작하는 채팅 봇 시스템 구현을 목표로 하였음
  - app 파티션과 main 파티션의 분리되지 않아 수정의 범위가 넓고 모호하였음. 명령어 등 확장이 어려웠음.
  - 테스트 코드 작성이 어렵거나 제한적으로 가능하였음. 리팩터링이 어려웠음.

- v2. 2021-12 - 2022.01
  - main 파티션 중 텔래그램과의 통신 모듈을 telegram-bot으로 분리하였음. 
  - telegram-bot은 TDD로 개발하였고 최대한 단순한 상태를 유지함.

- v3. 2021.01 - 2022.07
  - 어너테이션 기반의 확장 가능한 아키텍쳐를 구현.
  - 스프링의 DispatcherServlet을 참고하여 front controller pattern과 adapter pattern 기반의 프로젝트 구현. 
  - 클라이언트의 채팅 메시지에 대한 확장이 쉽고 자유로워 졌음
  - 다만 main 파티션과 app 파티션이 분리되지 않아 테스트가 어렵고 비니지스 로직이 분명하게 드러나지 않았음.

- v4. 2022.09 - 2022.11
  - telegram-framework와 dictionary-bot으로 프로젝트를 분리하여 main 파티션과 app 파티션을 완전하게 분리
  - 프레임워크로 동작. 의존성 주입과 몇 가지 간단한 설정을 통하여 클라이언트 개발자가 쉽게 사용할 수 있음. 새로운 채팅 봇 프로젝트 생성에 자유로움.
  - main과 app 파티션의 분리로 SRP를 준수. 테스트 코드 작성이 단순해지고 유지보수가 편해짐.
  - 오픈소스로 배포 (sonatype.org)

# 버전 로그 
- 0.4.2 오픈 소스 배포
- 0.4.3 java 8 버전 대응