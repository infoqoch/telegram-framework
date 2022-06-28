package infoqoch.dictionarybot.update.dispatcher.wrong1;

import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.body.UpdateChat;
import infoqoch.dictionarybot.update.resolver.param.mapper.UpdateRequestBodyParameterMapper;
import infoqoch.dictionarybot.update.resolver.param.mapper.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class WrongReturnController {

    // 지원하지 않는 데이터 타입에 대한 한정을 스프링 컨텍스트 로딩시점에서 제한할 필요가 있다. 현재는 런타임 시점에서 예외가 발생함.
    // 지금 지원은 어려움. 왜냐하면 param과 return의 resolver가 switch 형태를 가지고 있기 때문. 이를 수정한 후에 사용 가능.
    @Disabled("지원 필요")
    @Test
    void not_support_return_date_type(){
        assertThatThrownBy(()->{
            WrongUpdateDispatcherFactory.defaultInstance();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    // LocalDateTime은 지원하지 않는다.
    @UpdateRequestMethodMapper(EXCEL_PUSH)
    public LocalDateTime excelpush() {
        return LocalDateTime.now();
    }

    @UpdateRequestMethodMapper(LOOKUP_SENTENCE)
    public String lookupBySentence(UpdateRequest request) {
        StringBuilder sb = new StringBuilder();
        return "LOOKUP_SENTENCE : " + request.getValue();
    }

    @UpdateRequestMethodMapper(LOOKUP_DEFINITION)
    public UpdateResponse lookupByDefinition(UpdateRequest request) {
        return new UpdateResponse(SendType.MESSAGE, null);
    }

    @UpdateRequestMethodMapper(LOOKUP_WORD)
    public UpdateResponse lookupByWord(
            UpdateRequest updateRequest,
            @UpdateRequestBodyParameterMapper UpdateChat chat
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append(updateRequest.command()).append(" : ");
        sb.append(updateRequest.value()).append(" : ");
        sb.append(chat.getMessageId());

        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder(sb.toString()));
    }

    @UpdateRequestMethodMapper(HELP)
    public UpdateResponse help(UpdateRequest request) {
        return new UpdateResponse(SendType.MESSAGE, new MarkdownStringBuilder("help! " + request.getValue()));
    }

    @UpdateRequestMethodMapper(UNKNOWN)
    public String unknown() {
        return "unknown??";
    }


}
