package infoqoch.dictionarybot.main;

import infoqoch.dictionarybot.mock.bot.FakeTelegramSend;
import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.mock.repository.MemorySendRepository;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendRequest;
import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.dictionarybot.send.service.SendRunnerService;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static infoqoch.dictionarybot.send.Send.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

// DictionarySendRunner가 Telegram의 요청과 응답 데이터를 잘 분석함을 확인한다.
class SendRunnerTest {

    // compositions
    FakeTelegramSend telegramSend;

    // test target instance
    SendRunner runner;
    SendRepository repository;
    SendRunnerService sendRunnerService;

    @BeforeEach
    void setUp(){
        repository = new MemorySendRepository();
        sendRunnerService = new SendRunnerService(repository);
        telegramSend = new FakeTelegramSend();
        runner = new SendRunner(telegramSend,sendRunnerService);
    }
    
    @Test
    @DisplayName("정상적으로 요청하고 정상적으로 응답함")
    void message_send_success() {
        // given
        // mock response.
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w_hi", 1235l));

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, SendRequest.sendMessage(1235l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), REQUEST));

        // when
        runner.run();

        // then
        final List<Send> sends = repository.findByStatus(SUCCESS); // SUCCESS를 기대하며 그 값을 repository에서 호출한다.
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).result().getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    @DisplayName("발송 자체에서 에러가 발생함")
    void message_send_error() {
        // given
        // mock response... 데이터 전송 과정에서 예외가 발생하였음.
        telegramSend.setThrowRuntimeException(true);

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, SendRequest.sendMessage(324893249234l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST));

        // when
        runner.run();

        // then
        final List<Send> sends = repository.findByStatus(ERROR);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).result().getErrorMessage()).isEqualTo("예외닷!");
        assertThat(sends.get(0).getStatus()).isEqualTo(ERROR);
    }

    @Test
    @DisplayName("잘 발송하였으나 응답이 부정적임")
    void message_send_response_error() {
        // given
        // mock response... 데이터는 잘 전송하였으나, 텔레그램에서 bad request로 응답하였음.
        telegramSend.setMockMessageResponseJson("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, SendRequest.sendMessage(324893249234l, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST));

        // when
        runner.run();

        // then
        final List<Send> sends = repository.findByStatus(RESPONSE_ERROR);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).result().getErrorMessage()).isEqualTo("Bad Request: chat not found");
        assertThat(sends.get(0).getStatus()).isEqualTo(RESPONSE_ERROR);
    }
}