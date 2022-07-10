package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@TableGenerator(
        name = "dictionary_seq_generator",
        table = "dictionary_sequences",
        allocationSize = 1000)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dictionary {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionary_sequences")
    private Long no;

    @Embedded
    private DictionaryContent content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_user_no", nullable = false)
    private ChatUser chatUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private DictionarySource source;

    @Builder
    public Dictionary(Long no, ChatUser chatUser, DictionarySource source, DictionaryContent content)  {
        this.no = no;
        this.chatUser = chatUser;
        this.content = content.clone();
        this.source = source;
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
