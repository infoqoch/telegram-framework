package infoqoch.telegram.framework.update;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UpdateConfig.class)
public @interface EnableTelegramFramework {
}
