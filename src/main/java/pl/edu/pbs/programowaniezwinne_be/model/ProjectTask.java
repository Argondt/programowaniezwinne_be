package pl.edu.pbs.programowaniezwinne_be.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "project_task")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ProjectTask implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer storyPoint;

    @Size(min = 0, max = 1000, message = "Opis musi zawierać od {min} do {max} znaków!")
    @Column(length = 1000)
    private String description;

    @CreationTimestamp
    @Column(name = "dataczas_dodania", nullable = false, updatable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "project_zadania_list")
    private Project project;
    @Enumerated(EnumType.STRING)
    private ProjectTaskStatus status;

    private UUID userId;

}