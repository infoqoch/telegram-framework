package infoqoch.dictionarybot.update.controller;

import infoqoch.dictionarybot.model.user.ChatUser;
import infoqoch.dictionarybot.model.user.ChatUserRepository;
import infoqoch.telegrambot.util.MarkdownStringBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@Disabled("비지니스로직에 치명적인 부분은 아니므로 현재 생략한다. 프로젝트가 안정화 된 후 완성한다.")
public class ChatUserControllerTest {
    ChatUserController chatUserController;
    ChatUserRepository chatUserRepository;

    @BeforeEach
    void setUp(){
        chatUserRepository = mock(ChatUserRepository.class);
        chatUserController = new ChatUserController(chatUserRepository);
    }

    @DisplayName("status의 호출에 대한 응답 메시지가 정상적으로 동작하는지 확인한다.")
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
