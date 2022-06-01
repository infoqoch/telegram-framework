package infoqoch.dictionarybot.request.update;

import infoqoch.telegrambot.bot.entity.Chat;
import infoqoch.telegrambot.bot.entity.From;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Getter
@ToString
@Builder
public class ChatRequest {
    private Long updateId;
    private Long messageId;
    private Instant date;
    private String text;
    private From from;
    private Chat chat;
}
