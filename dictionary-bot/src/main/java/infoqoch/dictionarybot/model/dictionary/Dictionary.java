package infoqoch.dictionarybot.model.dictionary;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dictionary {
    public enum Source{
        NONE, EXCEL;
    }

    private Long no;
    private Source source;
    private String sourceId;

    private DictionaryContent content;

    @Builder
    public Dictionary(Long no, Source source, String sourceId, DictionaryContent content)  {
        this.no = no;
        this.content = content.clone();
        this.source = source == null ? Source.NONE : source;
    }
}
