package pl.edu.pbs.programowaniezwinne_be.controller;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTask;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTaskStatus;
import pl.edu.pbs.programowaniezwinne_be.service.ProjectTaskService;


@RestController
@RequestMapping("/api/zadania")
public class ZadanieRestController {

    private ProjectTaskService zadanieService;

    @Autowired
    public ZadanieRestController(ProjectTaskService zadanieService) {
        this.zadanieService = zadanieService;
    }

    @GetMapping("/{zadanieId}")
    ResponseEntity<ProjectTask> getTaskById(@PathVariable UUID zadanieId) {// pobieranie zadania
        return ResponseEntity.of(zadanieService.getProjectTask(zadanieId));
    }

    @PostMapping()
    public ResponseEntity<String> addTask(@Valid @RequestBody ProjectTaskDTO projectTaskDTO) {

        zadanieService.addTask(projectTaskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Zadanie zostało pomyślnie utworzone.");

    }

    @PutMapping("/{zadanieId}")
    public ResponseEntity<Void> updateTask(@Valid @RequestBody ProjectTaskUpdateDTO projectTaskDTO, @PathVariable UUID zadanieId) {
        boolean updateResult = zadanieService.updateTask(zadanieId, projectTaskDTO);
        if (!updateResult) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().build();
        }
    }


    @DeleteMapping("/{zadanieId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID zadanieId) {
        return zadanieService.getProjectTask(zadanieId).map(p -> {
            zadanieService.deleteTask(zadanieId);
            return new ResponseEntity<Void>(HttpStatus.OK);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping()
    Page<ProjectTask> getTasks(Pageable pageable) {
        return zadanieService.getTasks(pageable);
    }


    @GetMapping(value = "/projekt/{id}/zadania")
    List<ProjectTask> getTaskByProjectId(@PathVariable UUID id) {
        return zadanieService.searchByProjectId(id);
    }

    @PutMapping("/{zadanieId}/status")
    public ResponseEntity<ProjectTask> updateTaskStatus(@PathVariable UUID zadanieId, @RequestBody ProjectTaskStatus projectTaskDTO) {
        return ResponseEntity.ok(zadanieService.updateTaskStatus(zadanieId, projectTaskDTO));
    }
}
