package ua.profitsoft.library.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ua.profitsoft.library.dto.BookDetailsDto;
import ua.profitsoft.library.dto.UserDetailsDto;
import ua.profitsoft.library.models.BookType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomRepo {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<BookDetailsDto> findBooksByUserId(Long userId) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", userId);

        return jdbcTemplate.query("SELECT b.id_book as id, b.name, b.price, b.language, concat(a.name, ' ', a.surname) as author, b.publication_year, b.pages, b.isbn, b.type FROM library.book b\n" +
                "JOIN library.book_for_user u ON u.fk_book = b.id_book\n" +
                "JOIN library.author a ON a.id_author = b.fk_author\n" +
                "WHERE u.fk_user = :id", namedParameters, (rs, rowNum) -> toBookDetailsDto(rs));
    }

    private BookDetailsDto toBookDetailsDto (ResultSet rs) throws SQLException {
        return BookDetailsDto.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .language(rs.getString("language"))
                .author(rs.getString("author"))
                .publicationYear(rs.getString("publication_year"))
                .pages(rs.getInt("pages"))
                .ISBN(rs.getString("isbn"))
                .type(getBookType(rs.getInt("type")))
                .build();
    }

    private BookType getBookType (int type) {
        return (type == 0) ? BookType.PAPER : BookType.ELECTRONIC;
    }

    public List<UserDetailsDto> findUsersByRole (String role) {
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("role", role);

        return jdbcTemplate.query("SELECT u.id_user AS id, u.email, CONCAT(u.name, ' ', u.surname) AS name, " +
                        "u.birth_date AS birthDate, u.city, u.street, u.house, u.registration_date AS registrationDate,\n" +
                        "(SELECT SUM(b.quantity) FROM library.book_for_user b WHERE b.fk_user = u.id_user) AS bookCount\n" +
                        "FROM library.user u\n" +
                        "JOIN library.role r ON u.fk_role = r.id_role\n" +
                        "WHERE r.rolename = :role", namedParameters, (rs, rowNum) -> toUserDetailsDto(rs));
    }

    private static UserDetailsDto toUserDetailsDto (ResultSet rs) throws SQLException {
        return UserDetailsDto.builder()
                .id(rs.getLong("id"))
                .email(rs.getString("email"))
                .name(rs.getString("name"))
                .birthDate(rs.getString("birthDate"))
                .city(rs.getString("city"))
                .street(rs.getString("street"))
                .house(rs.getString("house"))
                .registrationDate(getDate(rs.getTimestamp("registrationDate")))
                .bookCount(rs.getInt("bookCount"))
                .build();
    }

    private static String getDate (Timestamp date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDateTime = dateFormat.format(date);

        return formattedDateTime;
    }
}
