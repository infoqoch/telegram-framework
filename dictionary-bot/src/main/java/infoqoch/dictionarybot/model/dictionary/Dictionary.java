package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.model.user.ChatUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Dictionary {

    @Id
    @SequenceGenerator(
            name="dictionary_sequence_generator",
            sequenceName="dictionary_sequence",
            initialValue=1
            // , allocationSize=1000 TODO 동작하지 아니함. 차후 확인 필요.
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE
            , generator = "dictionary_sequence_generator"
    )
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
}
