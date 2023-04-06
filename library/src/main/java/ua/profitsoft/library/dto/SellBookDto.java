package ua.profitsoft.library.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellBookDto {
    private Long userId;
    private Long bookId;
    private int quantity;
}
