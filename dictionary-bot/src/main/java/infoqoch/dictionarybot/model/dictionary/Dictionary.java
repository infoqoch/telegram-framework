package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dictionary {

    @Id @GeneratedValue
    private Long no;

    @Enumerated(EnumType.STRING)
    private InsertType insertType;

    @Embedded
    private DictionaryContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_id")
    private ChatUser chatUser;

    public enum InsertType {
        NONE, EXCEL;
    }

    @Builder
    public Dictionary(Long no, ChatUser chatUser, InsertType insertType, DictionaryContent content)  {
        this.no = no;
        this.chatUser = chatUser;
        this.content = content.clone();
        this.insertType = insertType == null ? InsertType.NONE : insertType;
    }

    public MarkdownStringBuilder toMarkdown() {
        return new MarkdownStringBuilder()
                .append(wordAndPronunciationMSB())
                .append(definitionAndSentenceMSB());
    }

    private MarkdownStringBuilder definitionAndSentenceMSB() {
        if(content.getSentence()==null) return null;
        return new MarkdownStringBuilder().append(definitionMSB()).plain(content.getSentence());
    }

    private MarkdownStringBuilder definitionMSB() {
        if(content.getDefinition() == null) return null;
        return new MarkdownStringBuilder().italic(content.getDefinition()).plain(", ");
    }

    private MarkdownStringBuilder wordAndPronunciationMSB() {
        if(content.getWord() == null) return null;
        return new MarkdownStringBuilder().bold(content.getWord()).append(pronunciationMSB()).lineSeparator();
    }

    private MarkdownStringBuilder pronunciationMSB() {
        if(content.getPronunciation() ==null) return null;
        return new MarkdownStringBuilder().plain("(").plain(content.getPronunciation()).plain(")");
    }
}
