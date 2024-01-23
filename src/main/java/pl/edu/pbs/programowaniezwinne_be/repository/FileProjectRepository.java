package pl.edu.pbs.programowaniezwinne_be.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.pbs.programowaniezwinne_be.model.FileProject;
import pl.edu.pbs.programowaniezwinne_be.model.Project;

import java.util.List;
import java.util.UUID;

public interface FileProjectRepository extends JpaRepository<FileProject, UUID> {
    List<FileProject> findByProject(Project project);
}