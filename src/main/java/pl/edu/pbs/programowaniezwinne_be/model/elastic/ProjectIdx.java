package pl.edu.pbs.programowaniezwinne_be.model.elastic;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Document(indexName = "projectidx")
@Getter
@Setter
public class ProjectIdx {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private UUID projectId;
    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String description;

}
