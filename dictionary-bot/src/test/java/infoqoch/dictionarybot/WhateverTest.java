package infoqoch.dictionarybot;

import org.junit.jupiter.api.Test;

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
}
