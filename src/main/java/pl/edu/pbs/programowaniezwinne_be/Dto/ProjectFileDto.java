package pl.edu.pbs.programowaniezwinne_be.Dto;

import java.util.UUID;

public record ProjectFileDto(
        UUID id,
        String fileName,
        String url
) {
}
