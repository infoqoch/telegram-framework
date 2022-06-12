package infoqoch.dictionarybot.send.response;

public class SendResponse {
    private final boolean ok;
    private final Object result;

    public SendResponse(boolean ok, Object result) {
        this.ok = ok;
        this.result = result;
    }

    public boolean isOk() {
        return ok;
    }

    public Object result() {
        return result;
    }
}
