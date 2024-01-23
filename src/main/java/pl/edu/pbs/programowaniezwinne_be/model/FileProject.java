package pl.edu.pbs.programowaniezwinne_be.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;
@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class FileProject implements Serializable {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String nazwaPliku;

    @Column(nullable = false, length = 1000)
    private String url;

    @ManyToOne
    @JoinColumn( nullable = false)
    private Project project;
}
