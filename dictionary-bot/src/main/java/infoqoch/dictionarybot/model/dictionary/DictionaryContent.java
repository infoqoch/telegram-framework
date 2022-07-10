package infoqoch.dictionarybot.model.dictionary;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor @Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class DictionaryContent implements Cloneable {
    private String word;
    private String pronunciation;
    private String partOfSpeech;
    private String quotation;
    private String definition;
    @Column(columnDefinition = "varchar(10000)")
    private String sentence;

    @Override
    protected DictionaryContent clone() {
        try{
            return (DictionaryContent) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
