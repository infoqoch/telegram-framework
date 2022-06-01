package infoqoch.dictionarybot.request.update;

import infoqoch.telegrambot.bot.entity.Chat;
import infoqoch.telegrambot.bot.entity.Document;
import infoqoch.telegrambot.bot.entity.From;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@Builder
public class DocumentRequest {
    private Long updateId;
    private Long messageId;
    private Instant date;
    private String caption;
    private From from;
    private Document document;
    private Chat chat;
}
