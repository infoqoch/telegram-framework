# Telegram - Dictionary bot

## 개요 
- 텔래그램 봇을 활용한 채팅 봇.
- 영어 공유 사전. 사전을 등록하고 사전을 검색한다. 
- 매시간 랜덤 사전, 어드민 등 기타 편의 기능 

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