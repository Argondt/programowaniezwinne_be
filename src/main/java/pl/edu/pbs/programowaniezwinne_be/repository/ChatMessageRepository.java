package pl.edu.pbs.programowaniezwinne_be.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pbs.programowaniezwinne_be.model.Chat.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {
    List<ChatMessage> findAllByOrderByTimestampAsc();

}
