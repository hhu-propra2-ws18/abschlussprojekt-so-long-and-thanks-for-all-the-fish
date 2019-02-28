package de.hhu.rhinoshareapp.chatTest;

import de.hhu.rhinoshareapp.domain.model.ChatMessage;
import de.hhu.rhinoshareapp.domain.service.ChatMessageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ChatmessageServiceTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    public void returnChatmessageByID(){
        ChatMessage chatMessage = ChatMessage.builder().build();
        testEntityManager.persist(chatMessage);
        testEntityManager.flush();

        ChatMessage found = chatMessageRepository.findById(chatMessage.getMessageID());

        assertThat(found).isEqualTo(chatMessage);
    }
}
