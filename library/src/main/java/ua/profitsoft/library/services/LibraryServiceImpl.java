package ua.profitsoft.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.profitsoft.library.dto.*;
import ua.profitsoft.library.models.*;
import ua.profitsoft.library.repository.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService{
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final AuthorRepository authorRepository;

    @Autowired
    private final BookRepository bookRepository;

    @Autowired
    private final CustomRepo customRepo;

    @Autowired
    private final BookForUserRepository bookForUserRepository;

    @Override
    public Long createClient(CreateUserDto userDto) {
        User savedUser = null;
        try {
            savedUser = userRepository.save(fromDto(userDto));
        } catch (ChangeSetPersister.NotFoundException e) {
            throw new RuntimeException(e);
        }
        return savedUser.getId();
    }

    @Override
    public void fillDatabase() throws ChangeSetPersister.NotFoundException {
        createRoles();
        createUsers();
        createAuthor();
        createBooks();
        bindBookToUser();
    }

    @Override
    public List<BookDetailsDto> getAllBooks() {
        List<BookDetailsDto> books = StreamSupport.stream(bookRepository.findAll().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .map(this::toBookDetailsDto)
                .toList();
        return books;
    }

    @Override
    public List<BookDetailsDto> findBooksByUserId(Long id) {
        return customRepo.findBooksByUserId(id);
    }

    @Override
    public BookTextDto findBookById(Long id) {
        return bookRepository.findById(id)
                .map(this::toBookTextDto)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Book with id '%s' not found".formatted(id.toString())));

    }

    @Override
    public List<UserDetailsDto> findAllUsers() {
        Role role = roleRepository.findByRolename("USER").orElseThrow(() ->
                new UsernameNotFoundException("Role with rolename USER not found"));
        return userRepository.findAllByRole(role)
                .stream()
                .map(this::toUserDetailsDto)
                .toList();
    }

    @Override
    public List<RoleDto> findAllRoles() {
        return StreamSupport.stream(roleRepository.findAll().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .map(this::toRoleDto)
                .toList();
    }

    @Override
    public List<AuthorDetailsDto> findAllAuthors() {
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
                .collect(Collectors.toList())
                .stream()
                .map(this::toAuthorDetailsDto)
                .toList();
    }

    @Override
    public void addBook(CreateBookDto bookDto) throws ChangeSetPersister.NotFoundException {
        bookRepository.save(Book.builder()
                .name(bookDto.getName())
                .price(bookDto.getPrice())
                .language(bookDto.getLanguage())
                .author(authorRepository.findById(bookDto.getAuthorId()).orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .publicationYear(bookDto.getPublicationYear())
                .pages(bookDto.getPages())
                .ISBN(bookDto.getISBN())
                .type(getBookType(bookDto.getType()))
                .text(bookDto.getText())
                .build());
    }

    @Override
    public UserShortInfoDto getUserById(Long userId) {
        return userRepository.findById(userId)
                .map(this::toUserShortInfoDto)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with id '%s' not found".formatted(userId.toString())));
    }

    @Override
    public void sellBook(SellBookDto sellBookDto) {
        bookForUserRepository.save(BookForUser.builder()
                .pk(BookForUserPK.builder()
                        .book(bookRepository.findById(sellBookDto.getBookId()).orElseThrow(() ->
                                new UsernameNotFoundException("Book with id '%s' not found".formatted(sellBookDto.getBookId().toString()))))
                        .user(userRepository.findById(sellBookDto.getUserId()).orElseThrow(() ->
                                new UsernameNotFoundException("User with id '%s' not found".formatted(sellBookDto.getUserId().toString()))))
                        .build())
                .quantity(sellBookDto.getQuantity())
                .build());
    }

    private void bindBookToUser() {
        bookForUserRepository.save(BookForUser.builder()
                .pk(BookForUserPK.builder()
                        .book(bookRepository.findById(1L).orElseThrow(() ->
                                new UsernameNotFoundException("Book with id 1 not found")))
                        .user(userRepository.findByEmail("user").orElseThrow(() ->
                                new UsernameNotFoundException("User with email user not found")))
                        .build())
                .quantity(1)
                .build());
        bookForUserRepository.save(BookForUser.builder()
                .pk(BookForUserPK.builder()
                        .book(bookRepository.findById(3L).orElseThrow(() ->
                                new UsernameNotFoundException("Book with id 3 not found")))
                        .user(userRepository.findByEmail("user").orElseThrow(() ->
                                new UsernameNotFoundException("User with email user not found")))
                        .build())
                .quantity(2)
                .build());
    }

    private void createRoles() {
        Role userRole = new Role();
        userRole.setRolename("USER");
        Role adminRole = new Role();
        adminRole.setRolename("ADMIN");
        Role apiRole = new Role();
        apiRole.setRolename("API");
        roleRepository.save(userRole);
        roleRepository.save(adminRole);
        roleRepository.save(apiRole);
    }

    private void createUsers () throws ChangeSetPersister.NotFoundException {
        userRepository.save(User.builder()
                .email("user")
                .password("{noop}123")
                .name("User")
                .surname("Patrik")
                .birthDate(java.sql.Date.valueOf(java.time.LocalDate.now(
                        java.time.ZoneId.of("Europe/Kiev"))))
                .registrationDate(Timestamp.valueOf(LocalDateTime.now()))
                .role(roleRepository.findByRolename("USER").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .city("Charkiv")
                .street("Sumska Street")
                .house("1")
                .enabled(true)
                .build()
        );
        userRepository.save(User.builder()
                .email("admin")
                .password("{noop}456")
                .name("Admin")
                .surname("Petrovich")
                .birthDate(java.sql.Date.valueOf(java.time.LocalDate.now(
                        java.time.ZoneId.of("Europe/Kiev"))))
                .registrationDate(Timestamp.valueOf(LocalDateTime.now()))
                .role(roleRepository.findByRolename("ADMIN").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .city("Charkiv")
                .street("Sumska Street")
                .house("1")
                .enabled(true)
                .build()
        );
        userRepository.save(User.builder()
                .email("api")
                .password("{noop}789")
                .name("")
                .surname("")
                .birthDate(java.sql.Date.valueOf(java.time.LocalDate.now(
                        java.time.ZoneId.of("Europe/Kiev"))))
                .registrationDate(Timestamp.valueOf(LocalDateTime.now()))
                .role(roleRepository.findByRolename("API").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .city("")
                .street("")
                .house("")
                .enabled(true)
                .build()
        );
    }

    private void createAuthor() {
        authorRepository.save(Author.builder()
                .name("Іван")
                .surname("Франко")
                .build()
        );
        authorRepository.save(Author.builder()
                .name("Тарас")
                .surname("Шевченко")
                .build()
        );
        authorRepository.save(Author.builder()
                .name("Леся")
                .surname("Українка")
                .build()
        );
    }

    private void createBooks() throws ChangeSetPersister.NotFoundException {
        bookRepository.save(Book.builder()
                .name("Кобзар")
                .price(380)
                .language("Українська")
                .author(authorRepository.findAuthorByNameAndSurname("Тарас", "Шевченко").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .publicationYear("2020")
                .pages(250)
                .ISBN("9788373637290")
                .text("Як умру, то поховайте\n" +
                        "Мене на могилі,\n" +
                        "Серед степу широкого,\n" +
                        "На Вкраїні милій,\n" +
                        "Щоб лани широкополі,\n" +
                        "І Дніпро, і кручі\n" +
                        "Було видно, було чути,\n" +
                        "Як реве ревучий.\n" +
                        "\n" +
                        "Як понесе з України\n" +
                        "У синєє море\n" +
                        "Кров ворожу... отоді я\n" +
                        "І лани і гори —\n" +
                        "Все покину і полину\n" +
                        "До самого бога\n" +
                        "Молитися... А до того —\n" +
                        "Я не знаю бога.\n" +
                        "\n" +
                        "Поховайте та вставайте.\n" +
                        "Кайдани порвіте\n" +
                        "І вражою злою кров'ю\n" +
                        "Волю окропіте.\n" +
                        "І мене в сім'ї великій,\n" +
                        "В сім'ї вольній, новій\n" +
                        "Не забудьте пом'янути\n" +
                        "Незлим тихим словом.")
                .type(BookType.PAPER)
                .build());
        bookRepository.save(Book.builder()
                .name("Украдене щастя")
                .price(102)
                .language("Українська")
                .author(authorRepository.findAuthorByNameAndSurname("Іван", "Франко").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .publicationYear("2022")
                .pages(50)
                .ISBN("9789660361966")
                .text("ДРАМА З СІЛЬСЬКОГО ЖИТТЯ в 5 діях.\n" +
                        "ДІЙОВІ ОСОБИ:\n" +
                        "Микола Задорожний, чоловік, літ 45, невеликого росту, похилий, рухи повільні.\n" +
                        "Анна, його жінка, молодиця, літ 25.\n" +
                        "Михайло Гурман, Жандарм, високий, здоровий мужчина, літ З0.\n" +
                        "Олекса Бабич, селянин, літ 40, сусіда Миколи.\n" +
                        "Настя, його жінка, літ 35.\n" +
                        "Війт, селянин, літ 50.\n" +
                        "Шльома, орендар.\n" +
                        "Селяни, селянки, парубки і дівчата, музики і т. і.\n" +
                        "\n" +
                        "Діється коло 1870 року в підгірськім селі Незваничах.\n" +
                        "ДІЯ ПЕРША\n" +
                        "Нутро сільської хати. Ніч. Надворі чути шум вітру, сніг б'є об вікна. В печі горить огонь, при нім горшки. Анна і Настя пораються коло печі. На лаві, на ослоні, на припічку і на печі дівчата і парубки, одні прядуть, другі мотають пряжу на мотовилах; насеред хати при стільці один парубок плете рукавиці, другий на коливороті крутить шнур.")
                .type(BookType.PAPER)
                .build());
        bookRepository.save(Book.builder()
                .name("Лісова пісня")
                .price(128)
                .language("Українська")
                .author(authorRepository.findAuthorByNameAndSurname("Леся", "Українка").orElseThrow(()->new ChangeSetPersister.NotFoundException()))
                .publicationYear("2022")
                .pages(120)
                .ISBN("9780674291874")
                .text("ПРОЛОГ\n" +
                        "\n" +
                        "Старезний, густий, предковічний ліс на Волині. Посеред лісу простора галява з плакучою березою і з великим прастарим дубом. Галява скраю переходить в куп'я та очерети, а в одному місці в яро-зелену драговину — то береги лісового озера, що утворилося з лісового струмка. Струмок той вибігає з гущавини лісу, впадає в озеро, потім, по другім боці озера, знов витікає і губиться в хащах. Саме озеро — тиховоде, вкрите ряскою та лататтям, але з чистим плесом посередині. Містина вся дика, таємнича, але не понура, — повна ніжної, задумливої поліської краси. Провесна. По узліссі і на галяві зеленіє перший ряст і цвітуть проліски та сон-трава. Дерева ще безлисті, але вкриті бростю, що от-от має розкритись. На озері туман то лежить пеленою, то хвилює од вітру, то розривається, одкриваючи блідо-блакитну воду.\n" +
                        "В лісі щось загомоніло, струмок зашумував, забринів, і вкупі з його водами з лісу вибіг \"Той, що греблі рве\" — молодий, дуже білявий, синьоокий, з буйними і разом плавкими рухами; одежа на йому міниться барвами, від каламутно-жовтої до ясно-блакитної, і поблискує гострими злотистими іскрами. Кинувшися з потоку в озеро, він починає кружляти по плесі, хвилюючи його сонну воду; туман розбігається, вода синішає.\n")
                .type(BookType.ELECTRONIC)
                .build());
    }

    private User fromDto (CreateUserDto userDto) throws ChangeSetPersister.NotFoundException {
        java.time.LocalDate todayLocalDate = java.time.LocalDate.now(
                java.time.ZoneId.of("Europe/Kiev"));
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword("{noop}" + userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setBirthDate(convert(todayLocalDate));
        user.setRole(roleRepository.findByRolename(userDto.getRole()).orElseThrow(()->new ChangeSetPersister.NotFoundException()));
        user.setRegistrationDate(Timestamp.from(Instant.now()));
        user.setCity(userDto.getCity());
        user.setStreet(userDto.getStreet());
        user.setHouse(userDto.getHouse());
        user.setEnabled(true);
        return user;
    }

    private BookDetailsDto toBookDetailsDto (Book book) {
        return BookDetailsDto.builder()
                .id(book.getId())
                .name(book.getName())
                .price(book.getPrice())
                .language(book.getLanguage())
                .author(book.getAuthor().getName() + " " + book.getAuthor().getSurname())
                .publicationYear(book.getPublicationYear())
                .pages(book.getPages())
                .ISBN(book.getISBN())
                .type(book.getType())
                .build();
    }

    private BookTextDto toBookTextDto (Book book) {
        return BookTextDto.builder()
                .id(book.getId())
                .name(book.getName())
                .author(book.getAuthor().getName() + " " + book.getAuthor().getSurname())
                .pages(book.getPages())
                .text(book.getText())
                .build();
    }

    private UserDetailsDto toUserDetailsDto (User user) {
        return UserDetailsDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName() + " " + user.getSurname())
                .birthDate(user.getBirthDate().toString())
                .city(user.getCity())
                .street(user.getStreet())
                .house(user.getHouse())
                .registrationDate(getDate(user.getRegistrationDate()))
                .build();
    }

    private RoleDto toRoleDto (Role role) {
        return RoleDto.builder()
                .rolename(role.getRolename())
                .build();
    }

    private static java.sql.Date convert(java.time.LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    private static String getDate (Timestamp date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = dateFormat.format(date);

        return formattedDateTime;
    }

    private AuthorDetailsDto toAuthorDetailsDto (Author author) {
        return AuthorDetailsDto.builder()
                .id(author.getId().toString())
                .name(author.getName() + " " + author.getSurname())
                .build();
    }

    private BookType getBookType (String type) {
        return (BookType.PAPER.toString() == type) ? BookType.PAPER : BookType.ELECTRONIC;
    }

    private UserShortInfoDto toUserShortInfoDto (User user) {
        return UserShortInfoDto.builder()
                .id(user.getId())
                .name(user.getName() + " " + user.getSurname())
                .email(user.getEmail())
                .build();
    }

    @Override
    public List<UserInfoDto> listUsers() {
        return userRepository.findAll().stream()
                .map(this::toInfoDto)
                .toList();
    }

    private UserInfoDto toInfoDto(User user) {
        return UserInfoDto.builder()
                .id(user.getId())
                .username(user.getEmail())
                .role(user.getRole().getRolename())
                .enabled(user.isEnabled())
                .build();
    }
}
