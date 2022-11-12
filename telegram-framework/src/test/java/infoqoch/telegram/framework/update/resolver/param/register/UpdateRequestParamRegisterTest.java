package infoqoch.telegram.framework.update.resolver.param.register;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegram.framework.update.UpdateRequestMapper;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegram.framework.update.resolver.param.UpdateDocumentUpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestUpdateRequestParam;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateRequestParamRegisterTest {
    UpdateRequestParamRegister returnRegister = new UpdateConfig(new AnnotationConfigApplicationContext(Config.class)).updateRequestParamRegister();

    @Test
    void support(){
        final Parameter[] params = getSampleParameters();

        assertThat(returnRegister.findSupportedResolverBy(params[0])).isInstanceOf(UpdateRequestUpdateRequestParam.class);
        assertThat(returnRegister.findSupportedResolverBy(params[1])).isInstanceOf(UpdateDocumentUpdateRequestParam.class);
//        assertThatThrownBy(()->
//                        returnRegister.findSupportedResolverBy(params[2])
//        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void markdownStringBuilder(){
        // give
        final UpdateRequest updateRequest = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help"));

        // when
        final UpdateRequestParam resolver = returnRegister.findSupportedResolverBy(getSampleParameters()[1]);
        final UpdateDocument result = (UpdateDocument) resolver.resolve(updateRequest);

        // then
        Assertions.assertThat(result.getCaption()).isEqualTo("/help");
    }

    @SneakyThrows
    private Parameter[] getSampleParameters() {
        final Method updateRequestMethod = Config.class.getDeclaredMethod("updateRequestMethod", UpdateRequest.class, UpdateDocument.class);
        final Parameter[] parameters = updateRequestMethod.getParameters();
        return parameters;
    }

    @Configuration
    @EnableTelegramFramework
    static class Config{
        @UpdateRequestMapper("hi")
        public String updateRequestMethod(UpdateRequest updateRequest, UpdateDocument updateDocument){
            return "hi!";
        }

        @UpdateRequestMapper("*")
        public  String any(){
            return "any";
        }
    }
}