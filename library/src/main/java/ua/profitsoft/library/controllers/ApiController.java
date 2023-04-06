package ua.profitsoft.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.profitsoft.library.dto.*;
import ua.profitsoft.library.models.User;
import ua.profitsoft.library.repository.UserRepository;
import ua.profitsoft.library.services.LibraryService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
public class ApiController {
    private final LibraryService libraryService;

    private final UserRepository userRepository;


//    @PostMapping("/createUser")
//    @ResponseStatus(HttpStatus.CREATED)
//    public RestResponse createUser(@RequestBody CreateUserDto userDto) {
//        Long id = libraryService.createClient(userDto);
//        return new RestResponse(String.valueOf(id));
//    }

    @GetMapping("/books")
    public List<BookDetailsDto> listBooks() {
        //return libraryService.findBooksByUserId(id);
        return libraryService.getAllBooks();
    }

//    @GetMapping("/users")
//    public List<UserInfoDto> getUsers() {
//        return libraryService.listUsers();
//    }

    @PostMapping("/getUsers")
    public RestResponse listUsers(@RequestBody UserDto dto) throws ChangeSetPersister.NotFoundException {
        User user = userRepository.findByEmailAndPassword(dto.getEmail(), dto.getPassword()).orElseThrow(()->new ChangeSetPersister.NotFoundException());
        if(user.getId() != 0)
        {
            return new RestResponse("OK");
        }
        else {
            return new RestResponse("Not Found");
        }
    }

    @GetMapping("/books/{id}")
    public BookTextDto myBooksPage (@PathVariable (value = "id") long id) {
        return libraryService.findBookById(id);
    }
}
