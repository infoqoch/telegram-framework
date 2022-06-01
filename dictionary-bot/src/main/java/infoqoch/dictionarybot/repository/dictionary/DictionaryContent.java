package infoqoch.dictionarybot.repository.dictionary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor @Builder
public class DictionaryContent implements Cloneable {
    private String word;
    private String pronunciation;
    private String partOfSpeech;
    private String source;
    private String definition;
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
