package infoqoch.telegramframework.spring.sampleinspring.log;

import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.body.UpdateDataType;
import infoqoch.telegram.framework.update.response.ResponseType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateLog {
    @Id @GeneratedValue
    private Long no;

    private Long chatId;
    private Long updateId;

    private String message;
    private String document;

    private String command;
    private String value;


    @Enumerated(EnumType.STRING)
    private ResponseType responseType;

    private UpdateLog(Long updateId, UpdateRequest updateRequest, UpdateResponse updateResponse) {
        this.chatId = updateRequest.chatId();
        this.updateId = updateId;
        this.command =  updateRequest.updateRequestCommandAndValue().getCommand().get();
        this.value = updateRequest.updateRequestCommandAndValue().getValue();
        this.responseType = updateResponse.getResponseType();
        this.message = updateRequest.input();
        if(updateRequest.updateDataType() == UpdateDataType.DOCUMENT){
            this.document = updateRequest.toDocument().getDocument().getFileId();
        }
    }

    public static UpdateLog of(Long updateId, UpdateRequest updateRequest, UpdateResponse updateResponse) {
        return new UpdateLog(updateId, updateRequest, updateResponse);
    }
}
