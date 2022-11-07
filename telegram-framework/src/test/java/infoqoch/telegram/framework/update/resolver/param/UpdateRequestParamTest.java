package infoqoch.telegram.framework.update.resolver.param;

import infoqoch.telegram.framework.update.UpdateRequestMethodMapper;
import infoqoch.telegram.framework.update.mock.MockUpdate;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDocument;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateRequestParamTest {
    @Test
    void resolver_updateRequest() throws NoSuchMethodException {
        // given
        final UpdateRequestUpdateRequestParam resolver = new UpdateRequestUpdateRequestParam();
        final Parameter[] parameters = getSampleParameters();

        // when - then 1
        // 서포트의 여부를 판단한다.
        assertThat(resolver.support(parameters[0])).isTrue();
        assertThat(resolver.support(parameters[1])).isFalse();

        // when - then 2
        // resolve 의 동작을 확인한다.
        final UpdateRequest updateRequest = MockUpdate.jsonToUpdateRequest(MockUpdate.chatJson("/help"));
        final Object result = resolver.resolve(updateRequest);
        assertThat(result).isInstanceOf(UpdateRequest.class);
    }

    @Test
    void resolver_document() throws NoSuchMethodException {
        // given
        final UpdateDocumentUpdateRequestParam resolver = new UpdateDocumentUpdateRequestParam();
        final Parameter[] parameters = getSampleParameters();

        // when - then 1
        // 서포트의 여부를 판단한다.
        assertThat(resolver.support(parameters[0])).isFalse();
        assertThat(resolver.support(parameters[1])).isTrue();

        // when 2
        // resolve 의 동작을 확인한다.
        final UpdateRequest updateRequest = MockUpdate.jsonToUpdateRequest(MockUpdate.documentJson("/help"));
        System.out.println("updateRequest = " + updateRequest);
        final Object result = resolver.resolve(updateRequest);

        // then 2
        assertThat(result).isInstanceOf(UpdateDocument.class);

        final UpdateDocument updateDocument = (UpdateDocument) result;
        assertThat(updateDocument.getCaption()).isEqualTo("/help");
    }

    private static Parameter[] getSampleParameters() throws NoSuchMethodException {
        final Method updateRequestMethod = Sample.class.getDeclaredMethod("updateRequestMethod", UpdateRequest.class, UpdateDocument.class);
        final Parameter[] parameters = updateRequestMethod.getParameters();
        return parameters;
    }

    static class Sample{
        @UpdateRequestMethodMapper("hi")
        void updateRequestMethod(UpdateRequest updateRequest, UpdateDocument updateDocumentUpdateRequestParam){

        }
    }
}
