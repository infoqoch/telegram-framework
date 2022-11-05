package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.UpdateConfig;
import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UpdateRequestParamRegisterTest {
    UpdateRequestParamRegister returnRegister = new UpdateConfig().updateRequestParamRegister();

    @Test
    void support(){
        final Parameter[] params = getSampleParameters();

        assertThat(returnRegister.findSupportedResolverBy(params[0])).isInstanceOf(UpdateRequestUpdateRequestParam.class);
        assertThat(returnRegister.findSupportedResolverBy(params[1])).isInstanceOf(UpdateDocumentUpdateRequestParam.class);
        assertThatThrownBy(()->
                        returnRegister.findSupportedResolverBy(params[2])
        ).isInstanceOf(IllegalArgumentException.class);
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
    private static Parameter[] getSampleParameters() {
        final Method updateRequestMethod = Sample.class.getDeclaredMethod("updateRequestMethod", UpdateRequest.class, UpdateDocument.class, LocalDateTime.class);
        final Parameter[] parameters = updateRequestMethod.getParameters();
        return parameters;
    }

    static class Sample{
        @UpdateRequestMethodMapper("hi")
        void updateRequestMethod(UpdateRequest updateRequest, UpdateDocument updateDocument, LocalDateTime localDateTime){

        }
    }
}