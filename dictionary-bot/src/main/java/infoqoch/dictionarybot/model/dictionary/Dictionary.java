package infoqoch.dictionarybot.model.dictionary;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Dictionary {
    private Long no;
    private Source source;
    private String sourceId;

    private DictionaryContent content;

    public enum Source{
        NONE, EXCEL;
    }

    @Builder
    public Dictionary(Long no, Source source, String sourceId, DictionaryContent content)  {
        this.no = no;
        this.content = content.clone();
        this.source = source == null ? Source.NONE : source;
    }

    public MarkdownStringBuilder toMarkdown() {
        return new MarkdownStringBuilder()
                .append(wordAndPronunciationMSB())
                .append(definitionAndSentenceMSB());
    }

    private MarkdownStringBuilder definitionAndSentenceMSB() {
        if(content.getSentence()==null) return null;
        return new MarkdownStringBuilder().append(definitionMSB()).plain(content.getSentence());
    }

    private MarkdownStringBuilder definitionMSB() {
        if(content.getDefinition() == null) return null;
        return new MarkdownStringBuilder().italic(content.getDefinition()).plain(", ");
    }

    private MarkdownStringBuilder wordAndPronunciationMSB() {
        if(content.getWord() == null) return null;
        return new MarkdownStringBuilder().bold(content.getWord()).append(pronunciationMSB()).lineSeparator();
    }

    private MarkdownStringBuilder pronunciationMSB() {
        if(content.getPronunciation() ==null) return null;
        return new MarkdownStringBuilder().plain("(").plain(content.getPronunciation()).plain(")");
    }

}
