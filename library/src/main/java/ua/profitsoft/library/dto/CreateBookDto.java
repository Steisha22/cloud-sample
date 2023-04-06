package ua.profitsoft.library.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookDto {
    private String name;

    private double price;

    private String language;

    private Long authorId;

    private String publicationYear;

    private int pages;

    private String ISBN;

    private String text;

    private String type;
}
