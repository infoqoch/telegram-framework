package infoqoch.telegramframework.spring.sampleinspring.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class User {
    @Id @GeneratedValue
    private Long no;

    private Long chatId;

    private String name;

    public User(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public void changeName(String value) {
        this.name = value;
    }
}
