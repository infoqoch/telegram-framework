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
        changeChatUser(chatUser);
        this.content = setDictionaryContent(content);
        this.source = source;
    }

    private DictionaryContent setDictionaryContent(DictionaryContent content) {
        if(content==null) throw new IllegalArgumentException("DictionaryContent is not nullable");
        return content.clone();
    }

    private void changeChatUser(ChatUser chatUser){
        if(chatUser==null) throw new IllegalArgumentException("ChatUser is not nullable");
        this.chatUser = chatUser;
        chatUser.addDictionary(this);
    }

    public MarkdownStringBuilder toMarkdown() {
        return new MarkdownStringBuilder().append(content.toMarkdown());
    }
}
