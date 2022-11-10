package infoqoch.dictionarybot.log.send.service;

import infoqoch.dictionarybot.log.send.SendLog;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import infoqoch.telegram.framework.update.response.SendType;
import infoqoch.telegram.framework.update.send.Send;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SendRunnerService {
    private final SendRepository sendRepository;

    public List<SendLog> findByStatusForScheduler(Send.Status status) {
        return sendRepository.findByStatus(status);
    }

    public List<SendLog> findByNoGreaterThanAndSendTypeForScheduler(long sendNo, SendType type) {
        return sendRepository.findByNoGreaterThanAndSendType(sendNo, type);
    }

    public Long maxNo() {
        return sendRepository.maxNo();
    }
}
