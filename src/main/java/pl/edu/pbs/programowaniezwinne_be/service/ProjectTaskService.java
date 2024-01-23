package pl.edu.pbs.programowaniezwinne_be.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTask;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTaskStatus;

public interface ProjectTaskService {
    Optional<ProjectTask> getProjectTask(UUID taskId);

    ProjectTask addTask(ProjectTaskDTO projectTask);
    void deleteTask(UUID taskId);
    Page<ProjectTask> getTasks(Pageable pageable);
    List<ProjectTask> searchByProjectId(UUID projectId);
     ProjectTask updateTaskStatus(UUID id, ProjectTaskStatus reservationState);
     boolean updateTask(UUID id, ProjectTaskUpdateDTO reservationState);

}
