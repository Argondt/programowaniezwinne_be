package pl.edu.pbs.programowaniezwinne_be.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTask;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTaskStatus;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ZadanieRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static pl.edu.pbs.programowaniezwinne_be.model.ProjectTaskStatus.TO_DO;


@Service
@Slf4j
public class ProjectTaskServiceImpl implements ProjectTaskService {

    private ZadanieRepository zadanieRepository;
    private ProjektRepository projektRepository;

    @Autowired
    public ProjectTaskServiceImpl(ZadanieRepository zadanieRepository, ProjektRepository projektRepository) {
        this.zadanieRepository = zadanieRepository;
        this.projektRepository = projektRepository;
    }

    @Override
    public Optional<ProjectTask> getProjectTask(UUID taskId) {
        return zadanieRepository.findById(taskId);
    }

    @Override
    public ProjectTask addTask(ProjectTaskDTO projectTaskDTO) {

        ProjectTask projectTask = new ProjectTask();
        projectTask.setName(projectTaskDTO.getNazwa());
        projectTask.setDescription(projectTaskDTO.getOpis());
        projectTask.setStatus(TO_DO);
        projectTask.setStoryPoint(Math.toIntExact(projectTaskDTO.getKolejnosc()));
        if (projectTaskDTO.getProjektId() != null) {
            Optional<Project> optionalProjekt = projektRepository.findById(projectTaskDTO.getProjektId());
            optionalProjekt.ifPresent(projectTask::setProject);
        }


        return zadanieRepository.save(projectTask);
    }

    @Override
    public void deleteTask(UUID zadanieId) {
        //TODO
    }

    @Override
    public Page<ProjectTask> getTasks(Pageable pageable) {
        return zadanieRepository.findAll(pageable);
    }


    @Override
    public List<ProjectTask> searchByProjectId(UUID projectId) {
        return zadanieRepository.findProjectTask(projectId);
    }

    @Override
    public ProjectTask updateTaskStatus(UUID id, ProjectTaskStatus reservationState) {
        return zadanieRepository.findById(id).map(zadanie -> {
            zadanie.setStatus(reservationState);
            return zadanieRepository.save(zadanie);
        }).orElseThrow(() -> new IllegalArgumentException("Project with id " + id + " not found"));
    }

    @Override
    public boolean updateTask(UUID id, ProjectTaskUpdateDTO projectTaskDTO) {
        Optional<ProjectTask> existingTask = zadanieRepository.findById(id);
        if (existingTask.isPresent()) {
            ProjectTask task = existingTask.get();
            task.setName(projectTaskDTO.getNazwa());
            task.setStoryPoint(Math.toIntExact(projectTaskDTO.getKolejnosc()));
            task.setDescription(projectTaskDTO.getOpis());
            zadanieRepository.save(task);
        }
        return false;
    }
}



