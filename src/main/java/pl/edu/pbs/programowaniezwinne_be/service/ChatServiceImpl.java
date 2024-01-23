package pl.edu.pbs.programowaniezwinne_be.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pbs.programowaniezwinne_be.model.Chat.ChatMessage;
import pl.edu.pbs.programowaniezwinne_be.repository.ChatMessageRepository;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<ChatMessage> getAllMessages() {
        return chatMessageRepository.findAllByOrderByTimestampAsc();
    }
}