package pl.edu.pbs.programowaniezwinne_be.Dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;
@Data
@Builder
public class ProjectTaskDTO{
        String nazwa;
        Long kolejnosc;
        String opis;
        UUID projektId;
}