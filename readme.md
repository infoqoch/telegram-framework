# Telegram - Dictionary bot

## 개요 
- 텔래그램 봇을 활용한 채팅 봇.
- 영어 공유 사전. 사전을 등록하고 사전을 검색한다. 
- 매시간 랜덤 사전, 어드민 등 기타 편의 기능

## 주요 문제 상황과 해소
### Telegram과의 통신에 대한 결합도를 낮춰야 함 : 파티션의 분리, 라이브러리를 통한 소스코드의 분리
- 텔래그램 봇을 기반으로 개발된 어플리케이션이기 때문에, 기본적으로 텔래그램 서버와의 IO에 의존적임. IO를 다루는 main 파티션과 비니지스 로직을 다루는 application 파티션과의 분리가 가장 중요한 문제였음.
- 메인 파티션은 infoqoch.dictionarybot.update 패키지에 배치하고, 어플리케이션 파티션은 infoqoch.dictionarybot.model.dictionary 패키지에 배치하였음. 메인와 어플리케이션의 교착점은 infoqoch.dictionarybot.update.controller 패키지에서 이뤄짐.
- 텔래그램과의 통신과 데이터 응답 및 수신 dto의 생성 및 해석을 현재의 어플리케이션에서 수행하지 않음. 별도의 라이브러리로 분리하여 dictionary-bot으로서의 요구사항에 집중할 수 있도록 코드를 최소화 하였음. 해당 라이브러리는 telegram-bot 임. 
- telegram-bot은 interface의 구현체로서, 커스텀 할 수 있고, 테스트할 수 있는 형태임.

### 단순한 채팅을 특정 명령으로 해석하고, 이를 적절한 로직으로 분배해야 함 : front controller pattern 
- 클라이언트는 어떤 메타 데이터가 존재하지 않는 단순한 문자로 명령. 서버는 해당 문자를 적절한 명령으로 해석하고 적합한 응답값을 보내야 함.
- 문자열의 명령을 해석하더라도 이를 처리하는 로직을 구현해야 함. 각각의 명령에 대한 로직은 확장 가능한 형태여야 함. 
- front controller pattern 을 활용하여 스프링의 dispatcher servlet 에 대응하는 우연한 아키텍쳐를 구현하였음.
  - 어너테이션을 기반으로 각각의 명령에 대해 손 쉽게 확장할 수 있음. 각 각의 로직은 리플렉션을 통해 스프링 컨텍스트 로딩 시점에서 검증하고 로딩함.    
  - Adapter pattern 을 활용해 필요로 한 값만을 파라미터로 받고, 적합한 형태의 리턴 타입을 선택할 수 있음.

### 공통 업무는 dispatcher 와 main 파티션에서
- dispatcher 는 텔래그램의 응답값을 파라미터로 전달하고, 적절한 응답값으로 생산하는 역할을 함. 이러한 응답값은 infoqoch.dictionarybot.main.UpdateRunner 에서 공통 처리하도록 구현.
- 예외 처리 역시 UpdateRunner 에서 공통 처리 

### 업무를 최대한 나눠서 각각의 도메인이 부담할 수 있도록 함
- 명령을 분석하고 front controller pattern 을 통해 적절한 응답값을 생산하였음. 이 때 udpate 패키지에서 처리하지 않고, 별도의 send 패키지를 구성하여 처리하도록 하였음. EventListener 를 활용하여 update(텔래그램 채팅분석) - send(텔래그램 응답) 간 결합을 최소화 하였음. 
- 현재 send는 DB에 요청값을 저장하고 초 단위로 동작하는 스케줄러를 통해 응답값을 보내는 형태임. 차후 메시징 처리 방식을 변경할 때, 이러한 업무의 분배를 하였으므로, 유연하게 대응할 수 있음.

### 테스트 주도 개발
- telegram-bot의 경우 interface의 구현체이기 때문에 대역을 생성하고, 그 대역을 컴퍼지션으로 주입하는 형태이기 때문에, 테스트에 용이함.
- front controller pattern 을 구현한 dispatcher 를 테스트 코드로 구현하였고, 유닛테스트 가능한 형태로 만들었음.
- 가능한 유닛 테스트를 위주로 개발하였음.

## 개발의 방향
- Front Controller Pattern 과 Adapter Pattern 등을 활용한 UpdateDispatcher 구현.
- 테스트주도개발
- 객체지향개발

## 어플리케이션의 흐름
- TelegramBot은 특정 채널의 채팅을 서버로 전달하며 이때 Update 타입으로 전달한다.
- Update는 어플리케이션이 이해할 수 있도록 번역된다. UpdateRequestMessage에 따라 Command와 Value으로 구분된 값으로 번역된다.
- UpdateRequestMessage는 UpdateRequestMethodMapper의 구현체에 따라, 클라이언트의 요구사항의 응답값을 UpdateResponse 타입으로 생성한다.
- UpdateResponse은 SendRequest로 변환된다.

## UpdateRequestMethodMapper과 UpdateDispatcher 
- 어플리케이션의 핵심은 UpdateDispatcher이다. front controller pattern을 적용하였다. 텔래그램 채팅 내용인 Update을 분석하고 데이터를 생산할 UpdateRequestMethodMapper을 선택한다.
  - UpdateRequestMethodMapper 는 리플렉션과 어너테이션을 기반으로 구현하여 main 파티션과 controller 간 의존성을 줄일 수 있었다.
  - 고객의 명령에 대한 로직을 적극적으로 구현할 수 있는 기반을 마련했다.
- UpdateDispatcher는 UpdateRequest와 UpdateResponse을 매개변수타입과 리턴타입으로 한다. 
  - apdater pattern을 적용하였다. Update 객체와 채팅의 응답값으로서 Send객체가 지원하는 한도 내에서 필요한 매개변수타입과 리턴타입을 자유롭게 선택할 수 있다.
- 어플리케이션 로딩 시점에서 로딩하고 에러를 잡는다.
  - 리플렉션이 런타임에서 동작할 경우 많은 리소스를 사용한다. 이를 방지하기 위하여 어플리케이션 로딩 시점에서 해당 객체를 초기화하고 오류 여부를 검증한다.

## TDD, 리팩터링
- 테스트주도로 개발을 지향하였음. 유닛테스트를 위주로 개발하였으며, 다수의 빈으로 조립되는 객체에 대해서는 적극적으로 대역 클래스를 구현하여 테스트를 진행하였음. (FakeController, FakeSendRequestEventListener 등)
- Jpa로 구현한 리포지토리에 대해서는 DataJpaTest를 하였으며, Service나 Runner에 대해서는 적극적으로 통합테스트를 진행하여, 배포 과정의 테스트를 최소화 하였음.
- 테스트코드를 기반으로 적극적인 리팩터링을 진행하였음.

## 사용법
- Telegram 을 설치한 다음, "BotFather" 봇을 찾는다. 봇을 생성하면, 해당 봇에 대한 토큰을 받는다. 토큰을 application.yml을   
- application.yml을 적절하게 수정한다. 특히 `# 입력해야 합니다.`로 주석처리된 부분은 반드시 입력한다. `telegram.token`에 입력한다.
- spring-data-jpa와 querydsl을 orm으로 사용 중이다. 특히 querydsl의 경우 설정이 복잡한데, 빌드 및 설치 후 gradle에서 compileQuerydsl을 호출해야 한다.