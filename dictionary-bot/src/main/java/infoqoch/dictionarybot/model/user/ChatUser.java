package infoqoch.dictionarybot.model.user;

import infoqoch.dictionarybot.model.dictionary.Dictionary;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
*
* 기본적인 흐름은 chat_id를 통해 움직인다.
* 단순한 사전 이용의 경우 chat_id로 사용하면 충분하다. 굳이 객체로 join을 하고 로깅을 할 때 해당 chat_id가 아닌 chatUserId로 저장하는 것은 리소스 낭비로 본다.
* chatUser의 경우 update - send의 흐름에 사용히가보다, 데이터 관리 차원을 위하여 사용한다. 예를 들면
*   - chatUser의 개인화 시스템 : 주기적인 스케줄러 제공
*   - 사전 노출의 범위 (자신의 사전을 타인에게 제공하지 않음)
*   - 회원 탈퇴 및 전체 삭제
*   - 차후 web을 통한 서비스 제공 시, 연동
*   - chatId가 변경되더라도 기존의 개인화 서비스 상태와 dictionary를 인계 받을 수 있도록 함.
*   - 기타 등등
*
*/
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
