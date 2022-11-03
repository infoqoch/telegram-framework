package infoqoch.dictionarybot;

import infoqoch.telegram.framework.update.UpdateDispatcher;
import org.junit.jupiter.api.Test;
import org.reflections.util.ClasspathHelper;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URL;
import java.util.Collection;

@SpringBootTest
public class DictionaryBotApplicationTests {

    @Test
    void contextLoads() {
        final Collection<URL> urls = ClasspathHelper.forPackage(UpdateDispatcher.class.getPackageName());
        for (URL url : urls) {
            System.out.println("url = " + url);
        }
    }

}
