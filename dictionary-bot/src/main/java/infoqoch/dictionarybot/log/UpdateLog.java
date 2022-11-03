package infoqoch.dictionarybot.log;

import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.request.UpdateRequest;
import infoqoch.telegram.framework.update.request.UpdateRequestCommand;
import infoqoch.telegram.framework.update.request.UpdateRequestCommandAndValue;
import infoqoch.telegram.framework.update.request.body.UpdateDataType;
import infoqoch.telegram.framework.update.response.UpdateResponse;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@AllArgsConstructor @Builder
public class UpdateLog {
    @Id @GeneratedValue
    private Long no;

    private Long updateId;
    private Long chatId;

    @Convert(converter = UpdateRequestCommandConvert.class)
    private UpdateRequestCommand updateCommand;
    private String updateValue;

    @Enumerated(EnumType.STRING)
    private UpdateDataType updateDataType;

    private String updateFileId;
    private String updateFileName;

    @Enumerated(EnumType.STRING)
    private SendType sendType;
    private String sendDocument;

    @Column(length = 10000)
    private String sendMessage;

    public static UpdateLog of(UpdateRequest updateRequest, UpdateResponse updateResponse) {
        final UpdateRequestCommandAndValue updateRequestCommandAndValue = updateRequest.updateRequestCommandAndValue();
        log.info("updateRequestCommandAndValue on UpdateLog#of : {}", updateRequestCommandAndValue);

        String fileId = null;
        String fileName = null;
        final UpdateDataType updateDataType = updateRequest.updateDataType();
        if(updateDataType==UpdateDataType.DOCUMENT){
            fileId = updateRequest.toDocument().getDocument().getFileId();
            fileName = updateRequest.toDocument().getDocument().getFileName();
        }

        return UpdateLog.builder()
                .updateId(updateRequest.updateId())
                .chatId(updateRequest.chatId())

                .updateCommand(updateRequestCommandAndValue.getCommand())
                .updateValue(updateRequestCommandAndValue.getValue())
                .updateDataType(updateRequest.updateDataType())

                .updateFileId(fileId)
                .updateFileName(fileName)

                .sendType(updateResponse.getSendType())
                .sendDocument(updateResponse.getDocument())
                .sendMessage(updateResponse.getMessage().toString())
                .build();
    }
}
