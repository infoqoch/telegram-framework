package infoqoch.dictionarybot.update.response;

public class UpdateResponse {
    SendType type;
    Object body;

    public UpdateResponse(SendType type, Object body) {
        this.type= type;
        this.body = body;
    }

    public SendType type() {
        return type;
    }

    public Object body(){
        return body;
    }

    public String message() {
        return "hi";
    }
}
