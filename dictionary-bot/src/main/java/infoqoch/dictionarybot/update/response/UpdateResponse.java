package infoqoch.dictionarybot.update.response;

import lombok.ToString;

@ToString
public class UpdateResponse {
    private final SendType type;
    private final Object body;

    private final String document;

    public UpdateResponse(SendType type, Object body) {
        this.type= type;
        this.body = body;
        this.document = document();
    }

    public UpdateResponse(SendType type, Object body, String document) {
        this.type = type;
        this.body = body;
        this.document = document;
    }

    public SendType type() {
        return type;
    }

    public Object body(){
        return body;
    }

    public String document(){
        return document;
    }
}
