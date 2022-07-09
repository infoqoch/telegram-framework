package infoqoch.dictionarybot.model.user;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChatUser {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long no;

    @Column(unique = true)
    private Long chatId;
    private String nickName;

    @OneToMany(mappedBy = "chatUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Dictionary> dictionaries = new ArrayList<>();

    public ChatUser(Long chatId, String nickName) {
        this.chatId = chatId;
        this.nickName = nickName;
    }

    public static ChatUser createUser(Long chatId, String nickName){
        return new ChatUser(chatId, nickName);
    }
}
