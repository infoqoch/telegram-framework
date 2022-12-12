package infoqoch.telegram.framework.update.util;

import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URL;

public class ReflectionUtil {
    public static void ifJarThenCalibrating(ConfigurationBuilder configuration, String basePackage) {
        if(!ClasspathHelper.forPackage(basePackage).stream().map(URL::toString).anyMatch(s -> s.startsWith("jar"))){
            configuration.filterInputsBy(new FilterBuilder().includePackage(basePackage));
        }
    }
}
