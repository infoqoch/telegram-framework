package infoqoch.dictionarybot.repository;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dictionary {
    private Long no;
    private DictionaryContent content;


    @Builder
    public Dictionary(Long no, DictionaryContent content)  {
        this.no = no;
        this.content = content.clone();
    }
}
