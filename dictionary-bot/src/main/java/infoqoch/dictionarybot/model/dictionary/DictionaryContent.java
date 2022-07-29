package infoqoch.dictionarybot.model.dictionary;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
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

    public MarkdownStringBuilder toMarkdown() {
        return new MarkdownStringBuilder()
                .append(wordAndPronunciationMSB())
                .append(definitionAndSentenceMSB());
    }

    private MarkdownStringBuilder definitionAndSentenceMSB() {
        if(getSentence()==null) return null;
        return new MarkdownStringBuilder().append(definitionMSB()).plain(getSentence());
    }

    private MarkdownStringBuilder definitionMSB() {
        if(getDefinition() == null) return null;
        return new MarkdownStringBuilder().italic(getDefinition()).plain(", ");
    }

    private MarkdownStringBuilder wordAndPronunciationMSB() {
        if(getWord() == null) return null;
        return new MarkdownStringBuilder().bold(getWord()).append(pronunciationMSB()).lineSeparator();
    }

    private MarkdownStringBuilder pronunciationMSB() {
        if(getPronunciation() ==null) return null;
        return new MarkdownStringBuilder().plain("(").plain(getPronunciation()).plain(")");
    }


    public boolean valid() {
        if (isEmpty(word) && isEmpty(definition) && isEmpty(sentence)) return false;
        return true;
    }

    private boolean isEmpty(String target) {
        return target == null || target.trim().length() == 0;
    }
}
