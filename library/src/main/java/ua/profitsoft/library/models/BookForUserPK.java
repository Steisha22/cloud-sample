package ua.profitsoft.library.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookForUserPK implements Serializable {
    @ManyToOne
    @JoinColumn(name = "fk_book", nullable = false)
    @JsonIgnore
    private Book book;

    @ManyToOne
    @JoinColumn(name = "fk_user", nullable = false)
    private User user;
}
