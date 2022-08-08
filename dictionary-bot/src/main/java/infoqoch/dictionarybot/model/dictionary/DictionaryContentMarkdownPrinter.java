package infoqoch.dictionarybot.model.dictionary;

import infoqoch.telegrambot.util.MarkdownStringBuilder;

public class DictionaryContentMarkdownPrinter {
    private final DictionaryContent dictionaryContent;

    public DictionaryContentMarkdownPrinter(Dictionary dictionary) {
        this.dictionaryContent = dictionary.getContent();
    }

    public MarkdownStringBuilder toMarkdown() {
        return new MarkdownStringBuilder()
                .append(wordAndPronunciationMSB())
                .append(definitionAndSentenceMSB());
    }

    MarkdownStringBuilder definitionAndSentenceMSB() {
        if (dictionaryContent.getSentence() == null) return null;
        return new MarkdownStringBuilder().append(definitionMSB()).plain(dictionaryContent.getSentence());
    }

    MarkdownStringBuilder definitionMSB() {
        if (dictionaryContent.getDefinition() == null) return null;
        return new MarkdownStringBuilder().italic(dictionaryContent.getDefinition()).plain(", ");
    }

    MarkdownStringBuilder wordAndPronunciationMSB() {
        if (dictionaryContent.getWord() == null) return null;
        return new MarkdownStringBuilder().bold(dictionaryContent.getWord()).append(pronunciationMSB()).lineSeparator();
    }

    MarkdownStringBuilder pronunciationMSB() {
        if (dictionaryContent.getPronunciation() == null) return null;
        return new MarkdownStringBuilder().plain("(").plain(dictionaryContent.getPronunciation()).plain(")");
    }
}