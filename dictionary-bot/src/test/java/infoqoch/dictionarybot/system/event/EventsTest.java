package infoqoch.dictionarybot.system.event;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EventsTest {
    @Test
    void event_raise(){
        // given
        final Sample sample = new Sample();
        assert sample.isCalled() == false;

        // when
        Events.raise(sample);

        //then
        assertThat(sample.isCalled()).isTrue();
    }
}