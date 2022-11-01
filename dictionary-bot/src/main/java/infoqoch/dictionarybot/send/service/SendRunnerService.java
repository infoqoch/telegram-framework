package infoqoch.dictionarybot.send.service;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.update.response.SendType;
import infoqoch.dictionarybot.send.repository.SendRepository;
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
