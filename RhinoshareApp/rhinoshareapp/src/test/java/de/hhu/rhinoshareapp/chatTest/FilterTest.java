package de.hhu.rhinoshareapp.chatTest;

import de.hhu.rhinoshareapp.chat.filter.FilterMessages;
import de.hhu.rhinoshareapp.chat.model.ChatMessage;
import de.hhu.rhinoshareapp.chat.service.ChatMessageRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
public class FilterTest {

    @MockBean
    ChatMessageRepository chatMessageRepository;

    public FilterMessages filter = new FilterMessages();

    public List<ChatMessage> oneToOne = new ArrayList<>();

    public List<ChatMessage> twoToOne = new ArrayList<>();

    public List<ChatMessage> oneToNone = new ArrayList<>();

    public List<ChatMessage> oneToTwo = new ArrayList<>();

    @Before
    public void onStartup() {
        ChatMessage firstMessage = new ChatMessage("Daniel", "Alex", "Hallo Alex!");
        ChatMessage secondMessage = new ChatMessage("Alex", "Daniel", "Hallo Daniel");
        ChatMessage thirdMessage = new ChatMessage("Nobody", "Sadman", "No one hears me?");
        ChatMessage fourthMessage  = new ChatMessage("Ralf", "Daniel", "Hallo Daniel!");

        ChatMessage oneToTwoMessageOne = new ChatMessage("Jens", "Christian", "Hey Leute!");
        ChatMessage oneToTwoMessageTwo = new ChatMessage("Jens", "David", "Hey Leute!");


        oneToOne.add(firstMessage);
        oneToOne.add(secondMessage);
        oneToOne.add(thirdMessage);

        twoToOne.add(secondMessage);
        twoToOne.add(thirdMessage);
        twoToOne.add(fourthMessage);

        oneToNone.add(fourthMessage);
        oneToNone.add(secondMessage);
        oneToNone.add(thirdMessage);

        oneToTwo.add(firstMessage);
        oneToTwo.add(oneToTwoMessageOne);
        oneToTwo.add(oneToTwoMessageTwo);

    }


    @Test
    public void oneToOne() {



        ChatMessageRepository chatMessageRepository = Mockito.mock(ChatMessageRepository.class);

        Mockito.when(chatMessageRepository.findAll()).thenReturn(oneToOne);

        List<ChatMessage> filteredlistFrom = filter.filterFrom("Daniel", chatMessageRepository);
        List<ChatMessage> filteredListTo = filter.filterTo("Daniel", chatMessageRepository);

        assertEquals(1, filteredlistFrom.size());

        assertEquals(1, filteredListTo.size());
    }

    @Test
    public void oneToTwo() {

        ChatMessageRepository chatMessageRepository = Mockito.mock(ChatMessageRepository.class);

        Mockito.when(chatMessageRepository.findAll()).thenReturn(oneToTwo);

        List<ChatMessage> filteredlistFrom = filter.filterFrom("Jens", chatMessageRepository);
        List<ChatMessage> filteredListTo = filter.filterTo("David", chatMessageRepository);
        List<ChatMessage> filteredListTo2 = filter.filterTo("Christian", chatMessageRepository);

        assertEquals(2, filteredlistFrom.size());

        assertEquals(1, filteredListTo.size());

        assertEquals(1, filteredListTo2.size());
    }

   @Test
    public void twoToOne() {

        ChatMessageRepository chatMessageRepository = Mockito.mock(ChatMessageRepository.class);

        Mockito.when(chatMessageRepository.findAll()).thenReturn(twoToOne);

        List<ChatMessage> filteredListTo = filter.filterTo("Daniel", chatMessageRepository);
        List<ChatMessage> filteredListFrom = filter.filterFrom("Ralf", chatMessageRepository);

        assertEquals(2, filteredListTo.size());
        assertEquals(1, filteredListFrom.size());
    }

    @Test
    public void oneToNone() {

        ChatMessageRepository chatMessageRepository = Mockito.mock(ChatMessageRepository.class);

        Mockito.when(chatMessageRepository.findAll()).thenReturn(oneToNone);

        List<ChatMessage> filteredListTo = filter.filterTo("Nobody", chatMessageRepository);
        List<ChatMessage> filteredListFrom = filter.filterFrom("Ralf", chatMessageRepository);

        assertEquals(0, filteredListTo.size());
        assertEquals(1, filteredListFrom.size());

    }


}
