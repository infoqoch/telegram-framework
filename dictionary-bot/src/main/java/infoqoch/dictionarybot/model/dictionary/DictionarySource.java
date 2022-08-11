package infoqoch.dictionarybot.model.dictionary;

import infoqoch.dictionarybot.model.user.ChatUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter()
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

    // 하나의 엑셀에서 수 만개의 dictionary가 생성될 수 있음. 그러므로 getter를 열지 아니함.
    // source의 관리를 위한 용도임.
    @Getter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    private List<Dictionary> addresses;

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
