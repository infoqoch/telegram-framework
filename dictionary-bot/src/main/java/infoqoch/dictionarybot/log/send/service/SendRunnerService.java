package infoqoch.dictionarybot.log.send.service;

import infoqoch.dictionarybot.log.send.Send;
import infoqoch.dictionarybot.log.send.repository.SendRepository;
import infoqoch.telegram.framework.update.response.SendType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SendRunnerService {
    private final SendRepository sendRepository;

    public List<Send> findByStatusForScheduler(Send.Status status) {
        return sendRepository.findByStatus(status);
    }

    public List<Send> findByNoGreaterThanAndRequestSendTypeForScheduler(long sendNo, SendType type) {
        return sendRepository.findByNoGreaterThanAndRequestSendType(sendNo, type);
    }

    public Long maxNo() {
        return sendRepository.maxNo();
    }
}
