package infoqoch.dictionarybot.controller.resolver;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.dictionarybot.update.request.UpdateRequest;
import infoqoch.dictionarybot.update.resolver.param.UpdateRequestParam;

import java.lang.reflect.Parameter;
import java.util.Optional;

public class UpdateChatUserRequestParam implements UpdateRequestParam {
    private final ChatUserRepository chatUserRepository;

    public UpdateChatUserRequestParam(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;

    }

    @Override
    public boolean support(Parameter target) {
        return target.getType() == ChatUser.class;
    }

    @Override
    public Object resolve(UpdateRequest request) {
        final Optional<ChatUser> chatUser = chatUserRepository.findByChatId(request.chatId());
        if(chatUser.isPresent())
            return chatUser.get();
        final ChatUser saved = chatUserRepository.save(new ChatUser(request.chatId(), request.toChat().getChat().getFirstName()));
        return saved;
    }
}
