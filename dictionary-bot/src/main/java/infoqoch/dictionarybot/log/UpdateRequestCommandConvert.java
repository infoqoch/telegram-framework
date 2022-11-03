package infoqoch.dictionarybot.log;

import infoqoch.telegram.framework.update.request.UpdateRequestCommand;

import javax.persistence.AttributeConverter;

public class UpdateRequestCommandConvert implements AttributeConverter<UpdateRequestCommand, String> {
    @Override
    public String convertToDatabaseColumn(UpdateRequestCommand attribute) {
        return attribute.get();
    }

    @Override
    public UpdateRequestCommand convertToEntityAttribute(String dbData) {
        return UpdateRequestCommand.of(dbData);
    }
}
