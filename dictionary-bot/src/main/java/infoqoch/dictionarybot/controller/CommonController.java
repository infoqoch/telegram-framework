package infoqoch.dictionarybot.controller;

import infoqoch.dictionarybot.system.properties.TelegramProperties;
import infoqoch.dictionarybot.update.request.UpdateRequestCommandAndValue;
import infoqoch.dictionarybot.update.resolver.UpdateRequestMethodMapper;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommonController {
    private final TelegramProperties telegramProperties;

    @UpdateRequestMethodMapper({"excel help", "help excel"})
    public UpdateResponse excel_help(UpdateRequestCommandAndValue message) {

        final MarkdownStringBuilder msb = new MarkdownStringBuilder()
                .bold("=== 💾 사전 등록 방법 (엑셀 📑) ===").lineSeparator()
                .plain("1. 전달드린 엑셀 형식에 맞춰 사전을 등록합니다. ").lineSeparator()
                .plain("2. 완성한 엑셀을 사전챗에 보냅니다. 설명에는 반드시 다음의 명령어를 입력해야 합니다. ").command("excel", "push").lineSeparator()
                .lineSeparator()
                .italic("다른 형태의 등록 방식은 차후 추가할 예정입니다.");

        return UpdateResponse.document(msb, telegramProperties.sampleExcelPush());
    }

    @UpdateRequestMethodMapper({"help", "start"})
    public MarkdownStringBuilder help(UpdateRequestCommandAndValue message) {
        log.info("UpdateRequestMethodMapper : help!");

        throwEx(message);

        if (message.getValue().equalsIgnoreCase("lookup")){
            return new MarkdownStringBuilder()
                    .bold("=== 🔎 확장된 검색 방법 ===").lineSeparator()
                    .command("w", "word").plain("  -> 단어 검색").lineSeparator()
                    .command("s", "sentence").plain("  -> 문장 검색").lineSeparator()
                    .command("d", "definition").plain("  -> 정의 검색").lineSeparator()
                    .command("f", "full search").plain("  -> 단어, 문장, 정의 통합 검색").lineSeparator()
                    .italic("  - 통합검색은 명령어 없이 단어만 검색해도 됩니다.").command("apple", null).italic("다만 명령어로 이미 설정된 단어(/w, /help) 는 불가능합니다.").lineSeparator()
                    .lineSeparator()
                    .bold("한국어로 명령어를 대체할 수 있습니다.").lineSeparator()
                    .command("ㅈ", "정의").lineSeparator()
                    .command("ㅈ", "definition").lineSeparator()
                    ;
        } else if (message.getValue().equalsIgnoreCase("extend")){
            return new MarkdownStringBuilder()
                    .bold("=== 확장🪐된 사전챗📚 사용방법 ===").lineSeparator()
                    .command("status", null).plain(" -> 자신의 상태값을 확인합니다.").lineSeparator()
                    .command("lookup_all_users", "Y").plain(" -> 단어를 검색할 때 모든 회원의 단어를 검색합니다.").lineSeparator()
                    .command("share_mine", "Y").plain(" -> 내가 등록한 단어를 모두에게 공유합니다.").lineSeparator()
                    .command("hourly", "Y").plain(" -> 08시부터 22시까지 매 시간 사전 하나를 보내드립니다.").lineSeparator()
                    ;
        }

        return new MarkdownStringBuilder()
                .bold("=== 사전챗📚 사용방법 ===").lineSeparator()
                .plain("사전을 등록💾하고 채팅으로 단어를 검색🔎합니다.").lineSeparator()
                .lineSeparator()
                .command("w", "word").plain(" -> 단어가 apple인 값을 검색합니다.").lineSeparator()
                .lineSeparator()
                .italic( "다음과 같이 명령해도 동작합니다.").plain(" (")
                .code("/w apple").plain(", ").code("w apple").plain(", ").code("w_apple").plain(") ")
                .italic("단어 검색과 더불어 모든 명령어는 같은 포맷으로 명령해도 동작합니다.").lineSeparator()
                .lineSeparator()
                .bold("확장된 기능은 아래의 설명을 참조 바랍니다.").lineSeparator()
                .command("help", "lookup").lineSeparator()
                .italic(" - 검색 기능을 살펴봅니다.").lineSeparator()
                .command("help", "extend").lineSeparator()
                .italic(" - 확장된 기능을 살펴봅니다.").lineSeparator()
                .command("excel", "help").lineSeparator()
                .italic(" - 나의 사전을 등록합니다.").lineSeparator()
                ;
    }

    private void throwEx(UpdateRequestCommandAndValue message) {
        if(message.getValue().equalsIgnoreCase("throw new IllegalArgumentException"))
            throw new IllegalArgumentException("테스트 용 인자 오류 발생");
        if(message.getValue().equalsIgnoreCase("throw new IllegalStateException"))
            throw new IllegalStateException("테스트 용 서버 오류 발생");
    }
}
