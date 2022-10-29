# Dictionary bot (Telegram) 

## 개요 
## 어플리케이션
- 텔래그램 봇을 활용한 채팅 봇
- 영어 공유 사전. 사전을 등록하고 사전을 검색한다.
- 매시간 랜덤 사전, 어드민, 다른 회원과의 사전 공유 기능 외 편의 기능 마련

## 아키텍쳐
### 확장성 
- front controller pattern 을 활용하여, 채팅 명령어를 처리하는 핸들러에 대한 확장에 열려있음.
- adapter pattern 을 활용하여, 핸들러의 파라미터 타입과 리턴 타입에 대한 확장에 열려있음.

### 의존성 분리
- 텔래그램과 통신하는 API는 외부 라이브러리로 분리 및 개발하였음. 어플리케이션과의 의존성을 분리함.
- 텔래그램의 채팅을 수신하는 update와 채팅에 대한 응답인 send, 그 외 스케쥴러 등 기능을 패키지 단위로 분리. Event와 DB를 통해 결합하여 최대한 결합도를 낮춤.

### TDD와 OOP를 지향
- 테스트 주도 개발

## 기술
- Java, Spring Boot 기반 개발
- oracle cloud를 어플리케이션로 사용 중이며, ubuntu에 배포
- spring-data-jpa 및 queryDSL을 ORM으로 사용

## 텔래그램 채팅의 분석에 대한 확장성을 위한 디자인 패턴 활용
### front controller pattern 과 UpdateDispatcher
- 웹 어플리케이션의 경우 기본적으로 url을 사용하여 요청에 대한 명령을 분류한다.
- 텔래그램의 경우 다음과 같은 형태로 명령과 값을 분류하며, 해당 명령에 대한 핸들러를 프론트 컨트롤러 패턴을 통해 확장 가능하도록 구현하였다.

- `w apple` 의 채팅을 어플리케이션가 분석할 경우... (참고로) 단어(word)를 기준으로 apple을 검색함을 의미한다.

```java
public enum UpdateRequestCommand {
    // LOOKUP_WORD를 통해 컨트롤러가 이해할 수 있는 명령을 enum으로 구현한다.
    // 해당 enum의 values를 통해 채팅의 첫 단어를 기준으로 enum이 무엇인지를 분류한다.
    LOOKUP_WORD({"w", "단어", "word"}); 
}

public class LookupController {
    // enum을 값으로 하는 어너테이션 @UpdateRequestMethodMapper 을 사용하여, 해당 명령에 대한 핸들러를 사용한다.
    @UpdateRequestMethodMapper(UpdateRequestCommand.LOOKUP_WORD)
    public List<Dictionary> lookupByWord(UpdateRequestMessage updateRequestMessage) {
        // 로직
    }
}
```

- `UpdateRequestCommand`의 enum과 values를 정의하고, 이에 맞춰 핸들러인 `@UpdateRequestMethodMapper(UpdateRequestCommand command)` 로 어너테이션을 사용한다. 스프링의 `@RequestMapping` 과 유사한 사용성을 제공한다.

### adapter pattern과 UpdateRequestMethodResolver
- 각 핸들러마다 필요로 한 데이터는 다르다. `w apple`로 전달했다면, 어플리케이션은 단순하게 해당 텍스트만을 필요로 할 수 있다. 하지만 경우에 따라 클라이언트가 전달한 사진이나 도큐먼트 파일이 필요할 수 있고, 클라이언트에 의존하는 로직을 구현하는 경우 클라이언트의 정보를 필요로 할 수 있다. 
- 리턴 값 또한 다 다르다. 응답값을 단순한 문자열로 전달할 수 있지만 사진이나 도큐먼트를 전달할 수 있다. 
- 이러한 파라미터 타입과 리턴 타입에 유연하게 대응하기 위하여 아답터 패턴을 적용하였고, 핸들러를 구현할 때 필요로 한 데이터 타입으로 정의할 수 있다.

- `w apple`의 값이 전달되었다고 가정하자.

```java
public class LookupController {
    // 단어 검색 핸들러(명령어 w에 대응함)를 실행한다.
    @UpdateRequestMethodMapper(UpdateRequestCommand.LOOKUP_WORD) 
    public List<Dictionary> lookupByWord(UpdateRequestMessage updateRequestMessage) {
        String value = updateRequestMessage.getValue();  // apple

        // word를 apple로 검색한 사전리스트를 응답한다.
        List<Dictionary> result = findDictionariesByWord(value);
        return result;
    }
}
```

- 파라미터 타입과 리턴 타입은 아래의 인터페이스로 추상화되어있다.

```java
public interface UpdateRequestParam {
    boolean support(Parameter target);

    Object resolve(UpdateRequest request);
}

public interface UpdateRequestReturn {
    boolean support(Method target);

    boolean support(Object target);

    UpdateResponse resolve(Object target);
}
```

- 프론트 컨트롤러 패턴에 활용했던 어너테이션 `@UpdateRequestMethodResolver` 으로 선언한 각각의 핸들러는 아래의 객체를 생성한다. 앞서 정의한 파라미터 타입과 리턴 타입에 대한 메타데이터를 리플렉션 기술을 통해 스프링 컨텍스트 로딩 때 수집한다.

```java
public class UpdateRequestMethodResolver {
    private final UpdateRequestMethodMapper mapper;
    private final UpdateRequestParam[] parameters;
    private final UpdateRequestReturn returnResolver;
}
```

### 이벤트와 DB 기반의 메시지 처리 : 결합도 낮추기
- 텔래그램은 `update` 라는 api를 통해 채팅 데이터를 어플리케이션에 전달한다. 어플리케이션은 해당 채팅에 대한 응답값을 `send` api를 통해 전달한다. 이와 같은 플로우에 의존할 경우, 어플리케이션에서는 `update`와 `send`를 처리하는 기능에 강한 결합이 발생한다. 이를 보완하기 위하여 이벤트 방식으로 두 기능을 연결하였으며, 스프링의 `ApplicationEventPublisher` 기능을 활용하였다.
- 오류에 대응하기 위한 Admin의 경고 메시지 기능, 매 시간마다 랜덤한 사전을 전달하는 기능을 구현하였다. 이는 DB를 활용하여 서비스를 구현하였다.

## 명령어 분석에 대하여
### 텔래그램 데이터 분석의 어려운 지점
- 웹 어플리케이션은 url과 헤더, 바디 등 그것의 역할이 분명하게 분리되어 있고, 스프링은 이에 대응하여 이를 잘 처리할 수 있도록 명확하고 깔끔하게 구현되어 있다. 
- 하지만 채팅의 경우 단순한 문자열로 구성되어 있다. 더 나아가 텔래그램의 경우 채팅을 전달하는 `update` api의 경우, 사용자의 상태나 문자, 사진, 도큐먼트 등 데이터 타입에 따라 객체와 프로퍼티가 마음대로 변경되어 json 문자열이 전달된다.
- 이처럼 텔래그램의 `update`를 처리하는 과정에서 1) 문자열의 처리 2) 객체와 프로퍼티가  마음대로 변경되는 json 의 처리가 매우 큰 문제였다.

### 문자열에 대한 우선적인 처리
- `update`를 단순하게 처리하기 위해서는 채팅의 문자열을 통해, 클라이언트의 요청사항을 수집하는 것으로 보았다. 클라이언트로 하여금 채팅 문자를 통해 요청사항을 명확하게 하고, 어플리케이션은 요청사항을 분석하는 형태로 구현하였다. 
- 어플리케이션은 앞서 설명한 `UpdateRequestCommand`을 enum을 통해 정의한다. `UpdateRequestCommand`은 `String[]`을 필드로 가진다. 사용자는 문자열의 첫 단어를 명령어로 하며, 어플리케이션 해당 단어를 기준으로 `UpdateRequestCommand`를 찾는다. 명령어를 제외한 문자열은 값으로 하여 `UpdateRequestMessage.value`로 전달한다.
- 어플리케이션은 `UpdateRequestCommand`에 대응하는 핸들러에, 클라이언트의 요청사항을 입력한다. 
- 어플리케이션은 해당 명령에 적합한 데이터를 파라미터를 통해 전달한다.

## 제한된 서비스 
- 현재까지 어플리케이션은 채팅 문자열에 대한 처리는 `/{커맨드}_{값}`의 포맷으로 제한 없이 제공하고 있다.
- 다만 어플리케이션이 지원하는 데이터 타입이 한정적이다. `update` api가 전달하는 데이터 타입 중, Chat(단순문자열), Document(사진 외 파일) 이 두 개에 대한 기능을 제공한다. 현재 구현한 사전봇은 두 개의 데이터타입만을 필요로 하기 때문이다. 그 이외의 데이터 타입에 대해서는 무시하거나 Chat으로 처리해버린다. 
- 텔래그램의 경우 채팅 기능 이외에 게임, 결제 서비스 등 다양한 기능이 있으며, 이를 봇 api 를 통해 제공 한다. 현재 어플리케이션은 차후 버전이 올라가더라도 채팅 수준의 기능에 한정하여 업데이트 될 예정이다.

## 나아가며
- 스프링은 웹 개발을 매우 편리하게 만들어 준다. 스프링은 http 통신에 대응하여 편리하고 다양한 기능을 제공한다. 인터셉터, 익셉션핸들러, 스프링데이터, 스프링시큐리티, 모델엔뷰 등 다양한 기능이 존재한다. 
- 스프링의 이러한 기능 중 백미는 단연 dispatcher servlet의 front controller patter이라 생각한다. `@Controller`와 `@RequestMapping`만을 붙이면 어플리케이션의 엔드포인트를 유연하고 확장 가능하게 구현할 수 있다. 
- 이러한 디스패쳐 서블릿을 손수 구현하고 싶었다. 이번 텔래그램 채팅 봇을 만들며 직접 아키텍쳐를 기획하고 구현할 수 있었다. 
- 물론 이런 과정은 매우 길었다. 프로젝트가 v3로 되어 있는 것을 볼 수 있었는데, 더 좋은 구조를 짜기 위하여 세 번을 갈아 엎었고 지금의 프로젝트가 만들어졌다. 조만간 한 번 더 엎을 것으로 생각된다.

