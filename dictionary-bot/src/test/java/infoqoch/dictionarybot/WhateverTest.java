package infoqoch.dictionarybot;

import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.Test;

import static infoqoch.dictionarybot.update.request.UpdateRequestCommand.SHARE_MINE;

public class WhateverTest {
    @Test
    void test(){

        final String s = resourceName("infoqoch.dictionarybot.update.controller");
        System.out.println("s = " + s);
    }
    private static String resourceName(String name) {
        if (name != null) {
            String resourceName = name.replace(".", "/");
            resourceName = resourceName.replace("\\", "/");
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }

            return resourceName;
        } else {
            return null;
        }
    }

    @Test
    void test2(){
        final MarkdownStringBuilder markdownStringBuilder = new MarkdownStringBuilder().bold("=나의 사전 공개 여부=").lineSeparator()
                .plain("Y 혹은 N으로 응답합니다.").lineSeparator()
                .command(SHARE_MINE.alias(), "Y").lineSeparator()
                .command(SHARE_MINE.alias(), "N").lineSeparator();
        System.out.println("markdownStringBuilder = " + markdownStringBuilder);
    }
}
