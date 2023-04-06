package ua.profitsoft.library.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "book")
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_book", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private String language;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_author", nullable = false)
    private Author author;

    @Column(name = "publication_year", nullable = false)
    private String publicationYear;

    @Column(nullable = false)
    private int pages;

    @Column(nullable = false)
    private String ISBN;

    @Column(nullable = false, length = 10000)
    private String text;

    @Column(nullable = false)
    private BookType type;
}
