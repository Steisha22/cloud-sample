package ua.profitsoft.library.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.profitsoft.library.dto.*;
import ua.profitsoft.library.models.BookType;
import ua.profitsoft.library.models.User;
import ua.profitsoft.library.repository.CustomRepo;
import ua.profitsoft.library.repository.UserRepository;
import ua.profitsoft.library.services.EmailService;
import ua.profitsoft.library.services.LibraryService;
import ua.profitsoft.library.services.PasswordGeneratorService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final LibraryService libraryService;

    private final UserRepository userRepository;

    private final CustomRepo customRepo;

    @Autowired
    private final PasswordGeneratorService passwordGeneratorService;

    @Autowired
    private final EmailService emailService;

    @GetMapping("/")
    public String mainPage(Model model) {
        try {
            libraryService.fillDatabase();
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }

        List<BookDetailsDto> books = libraryService.getAllBooks();
        model.addAttribute("allBooks", books);
        return "main";
    }

    @GetMapping("/home")
    public String homePage(Model model) throws ChangeSetPersister.NotFoundException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()->new ChangeSetPersister.NotFoundException());
        model.addAttribute("user", user.getId());
        return "home";
    }

    @GetMapping("/myBooks/{id}")
    @PreAuthorize("hasAuthority('PRIV_USER')")
    public String myBooksPage (@PathVariable (value = "id") long id, Model model) {
        List<BookDetailsDto> books = libraryService.findBooksByUserId(id);
        model.addAttribute("userId", id);
        model.addAttribute("usersBooks", books);
        return "myBooks";
    }

    @GetMapping("/myBooks/{userId}/book/{bookId}")
    @PreAuthorize("hasAuthority('PRIV_USER')")
    public String bookDetails (@PathVariable (value = "bookId") long bookId, Model model) {
        BookTextDto book = libraryService.findBookById(bookId);
        model.addAttribute("book", book);
        return "book";
    }

    @GetMapping("/allUsers")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String allUsersPage (Model model) {
        List<UserDetailsDto> users = customRepo.findUsersByRole("USER");
        model.addAttribute("allUsers", users);
        return "allUsers";
    }

    @GetMapping("/createUser")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String createUserPage (Model model) {
        CreateUserDto createUserDto = new CreateUserDto();
        List<RoleDto> roles = libraryService.findAllRoles();
        model.addAttribute("createUserDto", createUserDto);
        model.addAttribute("roles", roles);
        return "createUser";
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String createUser (@ModelAttribute("createUserDto") CreateUserDto createUserDto) {
        String password = passwordGeneratorService.generate(10);
        createUserDto.setPassword(password);
        libraryService.createClient(createUserDto);
        emailService.sendSimpleEmail(createUserDto.getEmail(), "Password for Library Service", "Your password is " + createUserDto.getPassword());
        return "redirect:/allUsers";
    }

    @GetMapping("/addBook")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String addBookPage (Model model) {
        CreateBookDto bookDto = new CreateBookDto();
        List<AuthorDetailsDto> authors = libraryService.findAllAuthors();
        List<BookType> bookTypes = List.of(BookType.PAPER, BookType.ELECTRONIC);
        model.addAttribute("bookTypes", bookTypes);
        model.addAttribute("authors", authors);
        model.addAttribute("bookDto", bookDto);
        return "addBook";
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String addBook (@ModelAttribute("bookDto") CreateBookDto createBookDto) throws ChangeSetPersister.NotFoundException {
        libraryService.addBook(createBookDto);
        return "redirect:/";
    }

    @GetMapping("/sellBook/{userId}")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String sellBookPage (@PathVariable (value = "userId") long userId, Model model) {
        UserShortInfoDto userDto = libraryService.getUserById(userId);
        List<BookDetailsDto> books = libraryService.getAllBooks();
        SellBookDto sellBookDto = new SellBookDto();
        String url = "sellBook/" + userId;
        model.addAttribute("url", url);
        model.addAttribute("user", userDto);
        model.addAttribute("allBooks", books);
        model.addAttribute("sellBookDto", sellBookDto);
        return "sellBook";
    }

    @PostMapping("/sellBook")
    @PreAuthorize("hasAuthority('PRIV_ADMIN')")
    public String sellBook (@ModelAttribute("sellBookDto") SellBookDto sellBookDto) {
        libraryService.sellBook(sellBookDto);
        return "redirect:/allUsers";
    }

}
