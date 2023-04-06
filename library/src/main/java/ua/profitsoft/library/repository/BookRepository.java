package ua.profitsoft.library.repository;

import org.springframework.data.repository.CrudRepository;
import ua.profitsoft.library.models.Book;

public interface BookRepository extends CrudRepository<Book, Long> {

}
