package ua.profitsoft.library.repository;

import org.springframework.data.repository.CrudRepository;
import ua.profitsoft.library.models.Author;

import java.util.Optional;

public interface AuthorRepository extends CrudRepository<Author, Long> {
    Optional<Author> findAuthorByNameAndSurname(String name, String surname);
}
