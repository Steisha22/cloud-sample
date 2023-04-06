package ua.profitsoft.library.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookTextDto {
    private Long id;

    private String name;

    private String author;

    private int pages;

    private String text;
}
