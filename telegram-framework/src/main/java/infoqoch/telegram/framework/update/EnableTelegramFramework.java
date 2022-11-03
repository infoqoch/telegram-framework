package infoqoch.telegram.framework.update;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(UpdateDispatcherConfig.class)
public @interface EnableTelegramFramework {
}
