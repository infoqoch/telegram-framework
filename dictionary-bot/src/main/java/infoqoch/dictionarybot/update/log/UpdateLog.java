package infoqoch.dictionarybot.update.log;

import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.request.UpdateRequestCommand;
import infoqoch.dictionarybot.update.request.UpdateRequestMessage;
import infoqoch.dictionarybot.update.request.body.UpdateDataType;
import infoqoch.dictionarybot.update.response.UpdateResponse;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@AllArgsConstructor @Builder
public class UpdateLog {
    @Id @GeneratedValue
    private Long no;

    private Long updateId;
    private Long chatId;

    @Enumerated(EnumType.STRING)
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
        final UpdateRequestMessage updateRequestMessage = updateRequest.updateRequestMessage();

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

                .updateCommand(updateRequestMessage.getCommand())
                .updateValue(updateRequestMessage.getValue())
                .updateDataType(updateRequest.updateDataType())

                .updateFileId(fileId)
                .updateFileName(fileName)

                .sendType(updateResponse.sendType())
                .sendDocument(updateResponse.document())
                .sendMessage(updateResponse.message().toString())
                .build();
    }
}
