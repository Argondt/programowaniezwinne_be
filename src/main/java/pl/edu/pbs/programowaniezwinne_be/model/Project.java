package pl.edu.pbs.programowaniezwinne_be.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "procject")
@Getter
@Setter
public class Project implements Serializable {
    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "project_id")
    private UUID id;

    @NotBlank(message = "Pole nazwa nie może być puste!")
    @Size(min = 3, max = 50, message = "Nazwa musi zawierać od {min} do {max} znaków!")
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = true, length = 1000)
    private String description;

    @CreationTimestamp
    @Column(name = "end_date")
    private LocalDate endDate;

    @CreationTimestamp
    @Column(name = "modification_time", nullable = false)
    private LocalDateTime modificationTime;

    @UpdateTimestamp
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @OneToMany()
    @JsonIgnoreProperties()
    private List<ProjectTask> zadaniaList;
//
//    @ManyToMany
//    @JoinTable(name = "projekt_student",
//            joinColumns = {@JoinColumn(name = "projekt_id")},
//            inverseJoinColumns = {@JoinColumn(name = "student_id")})
//    private Set<Student> studenci;


    public Project() {
        super();
    }

    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
}