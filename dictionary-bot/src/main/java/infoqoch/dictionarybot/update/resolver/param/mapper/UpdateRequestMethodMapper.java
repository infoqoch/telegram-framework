package infoqoch.dictionarybot.update.resolver.param.mapper;

import infoqoch.dictionarybot.update.request.UpdateRequestCommand;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UpdateRequestMethodMapper {
    UpdateRequestCommand value();
}
