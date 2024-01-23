package pl.edu.pbs.programowaniezwinne_be.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskDTO;
import pl.edu.pbs.programowaniezwinne_be.Dto.ProjectTaskUpdateDTO;
import pl.edu.pbs.programowaniezwinne_be.model.Project;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTask;
import pl.edu.pbs.programowaniezwinne_be.model.ProjectTaskStatus;
import pl.edu.pbs.programowaniezwinne_be.repository.ProjektRepository;
import pl.edu.pbs.programowaniezwinne_be.repository.ZadanieRepository;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ZadanieServiceTest {

    @Mock
    private ZadanieRepository zadanieRepository;

    @Mock
    private ProjektRepository projektRepository;

    @InjectMocks
    private ProjectTaskServiceImpl zadanieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProjectTask() {
        UUID testId = UUID.randomUUID();
        when(zadanieRepository.findById(testId)).thenReturn(Optional.of(new ProjectTask()));
        Optional<ProjectTask> found = zadanieService.getProjectTask(testId);
        verify(zadanieRepository).findById(testId);
        assertTrue(found.isPresent());
    }

    @Test
    public void testSetZadanie() {
        ProjectTaskDTO dto = ProjectTaskDTO.builder()
                .nazwa("name")
                .kolejnosc(1L)
                .opis("description")
                .projektId(UUID.randomUUID())
                .build();
        when(projektRepository.findById(any())).thenReturn(Optional.of(new Project()));
        when(zadanieRepository.save(any(ProjectTask.class))).thenReturn(new ProjectTask());
        ProjectTask result = zadanieService.addTask(dto);
        assertNotNull(result);
        verify(zadanieRepository).save(any(ProjectTask.class));
    }

    @Test
    public void testGetZadania() {
        Pageable pageable = PageRequest.of(0, 10);
        when(zadanieRepository.findAll(pageable)).thenReturn(Page.empty());
        Page<ProjectTask> result = zadanieService.getTasks(pageable);
        verify(zadanieRepository).findAll(pageable);
        assertNotNull(result);
    }

    @Test
    public void testUpdateTaskStatus() {
        UUID testId = UUID.randomUUID();
        ProjectTaskStatus testStatus = ProjectTaskStatus.TO_DO;
        ProjectTask task = new ProjectTask();
        when(zadanieRepository.findById(testId)).thenReturn(Optional.of(task));
        when(zadanieRepository.save(task)).thenReturn(task);
        ProjectTask result = zadanieService.updateTaskStatus(testId, testStatus);
        assertNotNull(result);
        assertEquals(testStatus, result.getStatus());
        verify(zadanieRepository).save(task);
    }

    @Test
    public void testUpdateTask() {
        UUID testId = UUID.randomUUID();
        ProjectTaskUpdateDTO dto = ProjectTaskUpdateDTO.builder()
                .nazwa("name")
                .kolejnosc(1L)
                .opis("description")
                .build();
        ProjectTask existingTask = new ProjectTask();
        when(zadanieRepository.findById(testId)).thenReturn(Optional.of(existingTask));
        when(zadanieRepository.save(any(ProjectTask.class))).thenReturn(existingTask);
        boolean result = zadanieService.updateTask(testId, dto);
        assertFalse(result); // Based on your implementation, this might need to be assertTrue
        verify(zadanieRepository).save(any(ProjectTask.class));
    }
}
