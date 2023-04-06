package ua.profitsoft.library.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserShortInfoDto {
    private Long id;
    private String email;
    private String name;
}
