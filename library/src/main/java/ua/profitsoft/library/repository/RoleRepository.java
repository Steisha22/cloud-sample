package ua.profitsoft.library.repository;

import org.springframework.data.repository.CrudRepository;
import ua.profitsoft.library.models.Role;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByRolename(String roleName);
}
