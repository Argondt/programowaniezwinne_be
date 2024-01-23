package pl.edu.pbs.programowaniezwinne_be.model.Chat;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import pl.edu.pbs.programowaniezwinne_be.model.Project;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "chat_message")
public class ChatMessage {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "message_id")
    private UUID messageId;

    @Column(nullable = false, length = 1000)
    private String content;

    private String sender;

    @Column(nullable = false)
    private LocalDateTime timestamp;

 
}
