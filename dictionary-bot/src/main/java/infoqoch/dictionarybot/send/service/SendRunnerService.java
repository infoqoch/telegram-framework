package infoqoch.dictionarybot.send.service;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.SendType;
import infoqoch.dictionarybot.send.repository.SendJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SendRunnerService {
    private final SendJpaRepository sendRepository;

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
