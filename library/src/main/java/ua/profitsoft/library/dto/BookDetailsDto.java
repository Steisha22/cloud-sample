package ua.profitsoft.library.dto;

import lombok.*;
import ua.profitsoft.library.models.BookType;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailsDto {
    private Long id;

    private String name;

    private double price;

    private String language;

    private String author;

    private String publicationYear;

    private int pages;

    private String ISBN;

    private BookType type;
}
