package ua.profitsoft.library.repository;

import org.springframework.data.repository.CrudRepository;
import ua.profitsoft.library.models.BookForUser;
import ua.profitsoft.library.models.BookForUserPK;

public interface BookForUserRepository extends CrudRepository<BookForUser, BookForUserPK> {
}
