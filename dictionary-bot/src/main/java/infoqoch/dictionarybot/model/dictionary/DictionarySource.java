package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.model.user.ChatUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
public class DictionarySource {
    @Id
    @GeneratedValue
    private Long no;

    private String fileId;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne(fetch = FetchType.LAZY)
    private ChatUser chatUser;

    @CreatedDate
    private LocalDateTime createdDate;

    public enum Type{
        EXCEL
    }

    public DictionarySource(String fileId, Type type, ChatUser chatUser) {
        this.fileId = fileId;
        this.chatUser = chatUser;
        this.type = type;
    }
}
