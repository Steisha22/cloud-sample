package ua.profitsoft.library.models;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "book_for_user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookForUser {
    @EmbeddedId
    @Column(nullable = false)
    private BookForUserPK pk;
    @Column(nullable = false)
    private int quantity;
}
