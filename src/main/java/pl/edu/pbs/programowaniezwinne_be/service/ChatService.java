package pl.edu.pbs.programowaniezwinne_be.service;

import org.springframework.stereotype.Service;
import pl.edu.pbs.programowaniezwinne_be.model.Chat.ChatMessage;

import java.util.List;

@Service
public interface ChatService {
    List<ChatMessage> getAllMessages();
}
