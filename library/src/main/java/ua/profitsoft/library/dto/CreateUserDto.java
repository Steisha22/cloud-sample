package ua.profitsoft.library.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {
    private String email;
    private String password;
    private String name;
    private String surname;
    private String birthDate;
    private String role;
    private String city;
    private String street;
    private String house;
}
