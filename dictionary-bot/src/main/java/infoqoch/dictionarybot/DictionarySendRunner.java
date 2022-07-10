package infoqoch.dictionarybot;

import infoqoch.dictionarybot.send.Send;
import infoqoch.dictionarybot.send.repository.SendRepository;
import infoqoch.telegrambot.bot.TelegramSend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DictionarySendRunner {
    private final TelegramSend telegramSend;
    private final SendRepository sendRepository;

    @Scheduled(fixedDelay = 500)
    @Transactional
    public void run() {
        List<Send> sendRequests = sendRepository.findByStatus(Send.Status.REQUEST);

        for (Send send : sendRequests) {
            try{
                send.sending(telegramSend);
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
    }
}
