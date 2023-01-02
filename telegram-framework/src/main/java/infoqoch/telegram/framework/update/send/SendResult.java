package infoqoch.telegram.framework.update.send;

import lombok.Getter;

@Getter
public class SendResult {
    private final Send send;

    public SendResult(Send send) {
        this.send = send;
    }
}
