package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ChatUserControllerTest {
    ChatUserController chatUserController;
    ChatUserRepository chatUserRepository;

    @BeforeEach
    void setUp(){
        chatUserRepository = mock(ChatUserRepository.class);
        chatUserController = new ChatUserController(chatUserRepository);
    }

    // TODO
    // msb 프린트 관련해서는 차후 수정 필요.
    @Disabled
    @Test
    void myStatus_print_test(){
        // given
        ChatUser chatUser  = ChatUser.createUser(1234l, "kim");

        // when
        final MarkdownStringBuilder result = chatUserController.myStatus(chatUser);

        // then
        assertThat(result.toString()).contains("*\\=\\=나의 상태\\=\\=*", "모든 회원 검색 여부 : Y", "_ 수정 : _/lookup\\_all\\_users\\_N", "사전 공개 여부 : Y", "_ 수정 : _/share\\_mine\\_N", "등록한 사전의 갯수 : 0");
    }
}
