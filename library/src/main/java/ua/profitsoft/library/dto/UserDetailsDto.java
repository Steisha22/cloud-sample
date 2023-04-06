package ua.profitsoft.library.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDetailsDto {
    private Long id;
    private String email;
    private String name;
    private String birthDate;
    private String city;
    private String street;
    private String house;
    private String registrationDate;
    private int bookCount;
}
