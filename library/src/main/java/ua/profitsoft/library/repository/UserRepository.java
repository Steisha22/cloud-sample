package ua.profitsoft.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.profitsoft.library.models.Role;
import ua.profitsoft.library.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByRole(Role role);

    Optional<User> findByEmail(String username);

    Optional<User> findByEmailAndPassword(String email, String password);
}
