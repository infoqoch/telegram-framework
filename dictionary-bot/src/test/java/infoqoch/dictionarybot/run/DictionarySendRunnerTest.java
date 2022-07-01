package infoqoch.dictionarybot.run;

import infoqoch.dictionarybot.DictionarySendRunner;
import infoqoch.dictionarybot.mock.data.MockSendResponse;
import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendDispatcher;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.send.repository.MemorySendRepository;
import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.dictionarybot.send.request.SendRequest;
import infoqoch.telegrambot.bot.TelegramBot;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static infoqoch.dictionarybot.send.Send.Status.*;
import static org.assertj.core.api.Assertions.assertThat;

class DictionarySendRunnerTest {

    // compositions
    TelegramBot bot;
    FakeTelegramSend telegramSend;

    SendDispatcher sendDispatcher;

    // test target instance
    DictionarySendRunner runner;
    SendRepository repository;

    @BeforeEach
    void setUp(){
        repository = new MemorySendRepository();

        telegramSend = new FakeTelegramSend();

        bot = new FakeTelegramBot(null, telegramSend);
        sendDispatcher = new SendDispatcher(bot);
        runner = new DictionarySendRunner(sendDispatcher,repository);
    }
    
    @Test
    void message_send_success() {
        // given
        // mock response.
        telegramSend.setMockMessageResponseJson(MockSendResponse.sendMessage("/w_hi", 1235l));

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, new SendRequest(1235l, SendType.MESSAGE, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST));

        // when
        runner.run();

        // then
        final List<Send> sends = repository.findByStatus(SUCCESS);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).getErrorMessage()).isEqualTo(null);
        assertThat(sends.get(0).getStatus()).isEqualTo(SUCCESS);
    }

    @Test
    void message_send_response_error() {
        // given
        // mock response... 데이터는 잘 전송하였으나, 텔레그램에서 bad request로 응답하였음.
        telegramSend.setThrowRuntimeException(true);

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, new SendRequest(324893249234l, SendType.MESSAGE, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST));

        // when
        runner.run();

        // then
        final List<Send> sends = repository.findByStatus(ERROR);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).getErrorMessage()).isEqualTo("예외닷!");
        assertThat(sends.get(0).getStatus()).isEqualTo(ERROR);
    }

    @Test
    void message_send_server_error() {
        // given
        // mock response... 데이터는 잘 전송하였으나, 텔레그램에서 bad request로 응답하였음.
        telegramSend.setMockMessageResponseJson("{\"ok\":false,\"error_code\":400,\"description\":\"Bad Request: chat not found\"}");

        // REQUEST 상태의 값이 리포지터리에 대기 중이다.
        repository.save(new Send(1l, new SendRequest(324893249234l, SendType.MESSAGE, new MarkdownStringBuilder().plain("hi의 검색결과는 다음과 같습니다")), Send.Status.REQUEST));

        // when
        runner.run();

        // then
        final Optional<Send> byNo = repository.findByNo(1l);
        System.out.println("byNo.get() = " + byNo.get());


        final List<Send> sends = repository.findByStatus(RESPONSE_ERROR);
        assertThat(sends).size().isEqualTo(1);
        assertThat(sends.get(0).getErrorMessage()).isEqualTo("Bad Request: chat not found");
        assertThat(sends.get(0).getStatus()).isEqualTo(RESPONSE_ERROR);
    }

}