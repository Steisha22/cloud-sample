package ua.profitsoft.library.services;

import org.springframework.data.crossstore.ChangeSetPersister;
import ua.profitsoft.library.dto.*;

import java.util.List;

public interface LibraryService {
    Long createClient (CreateUserDto user);

    void fillDatabase () throws ChangeSetPersister.NotFoundException;

    List<BookDetailsDto> getAllBooks ();

    List<BookDetailsDto> findBooksByUserId (Long userId);

    BookTextDto findBookById (Long id);

    List<UserDetailsDto> findAllUsers ();

    List<RoleDto> findAllRoles ();


    List<AuthorDetailsDto> findAllAuthors ();

    void addBook (CreateBookDto bookDto) throws ChangeSetPersister.NotFoundException;

    UserShortInfoDto getUserById (Long userId);

    void sellBook (SellBookDto sellBookDto);

    List<UserInfoDto> listUsers();
}
