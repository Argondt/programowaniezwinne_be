package pl.edu.pbs.programowaniezwinne_be.Dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class StudentDetailsDto {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String indexNumber;
    List<String> roles;

}
