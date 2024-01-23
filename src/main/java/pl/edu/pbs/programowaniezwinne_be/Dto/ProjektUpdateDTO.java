package pl.edu.pbs.programowaniezwinne_be.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@Builder
public class ProjektUpdateDTO {
    private String nazwa;
    private String opis;
}
