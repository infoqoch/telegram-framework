package infoqoch.telegram.framework.update.resolver.customparam;

import infoqoch.telegram.framework.update.EnableTelegramFramework;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import infoqoch.telegram.framework.update.resolver.custom.CustomUpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateDocumentUpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParam;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestParamRegister;
import infoqoch.telegram.framework.update.resolver.param.UpdateRequestUpdateRequestParam;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CustomUpdateRequestParamRegisterTest {
    UpdateRequestParamRegister returnRegister;

    @BeforeEach
    void setUp(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(Config.class);
        returnRegister = ac.getBean(UpdateRequestParamRegister.class);
    }

    @Test
    void support(){
        final Parameter[] params = getSampleParameters();
        assertThat(returnRegister.findSupportedResolverBy(params[0])).isInstanceOf(UpdateRequestUpdateRequestParam.class);
        assertThat(returnRegister.findSupportedResolverBy(params[1])).isInstanceOf(UpdateDocumentUpdateRequestParam.class);
        assertThat(returnRegister.findSupportedResolverBy(params[2])).isInstanceOf(SampleUpdateRequestParam.class);
    }

    @Test
    void custom_resolver(){
        // give
        final UpdateRequest updateRequest = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help"));

        // when
        final UpdateRequestParam resolver = returnRegister.findSupportedResolverBy(getSampleParameters()[2]);
        final Sample result = (Sample) resolver.resolve(updateRequest);

        // then
        assertThat(result.getUpdateId()).isEqualTo(updateRequest.updateId());
        assertThat(result.getRegDt().toLocalDate()).isToday();
    }

    @Test
    void default_resolver(){
        // give
        final UpdateRequest updateRequest = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help"));

        // when
        final UpdateRequestParam resolver = returnRegister.findSupportedResolverBy(getSampleParameters()[1]);
        final UpdateDocument result = (UpdateDocument) resolver.resolve(updateRequest);

        // then
        assertThat(result.getCaption()).isEqualTo("/help");
    }

    @SneakyThrows
    private static Parameter[] getSampleParameters() {
        final Method updateRequestMethod = SampleHandler.class.getDeclaredMethod("updateRequestMethod", UpdateRequest.class, UpdateDocument.class, Sample.class);
        final Parameter[] parameters = updateRequestMethod.getParameters();
        return parameters;
    }


    @Configuration
    @ComponentScan
    @EnableTelegramFramework
    static class Config implements CustomUpdateRequestParamRegister {

        @Bean
        public SampleHandler sampleHandler(){
            return new SampleHandler();
        }

        @Override
        public List<UpdateRequestParam> paramRegister() {
            return Arrays.asList(new SampleUpdateRequestParam());
        }
    }

}