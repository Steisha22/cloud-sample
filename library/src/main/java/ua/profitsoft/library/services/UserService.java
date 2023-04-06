package ua.profitsoft.library.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.profitsoft.library.models.Role;
import ua.profitsoft.library.models.User;
import ua.profitsoft.library.repository.RoleRepository;
import ua.profitsoft.library.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;
//    @PostConstruct
//    public void init() throws ChangeSetPersister.NotFoundException {
//            libraryService.fillDatabase();
//    }

//    public List<UserInfoDto> listUsers() {
//        return userRepository.findAll().stream()
//                .map(this::toInfoDto)
//                .toList();
//    }
//
//    private UserInfoDto toInfoDto(User user) {
//        return UserInfoDto.builder()
//                .id(user.getId())
//                .username(user.getEmail())
//                .role(user.getRole().getRolename())
//                .enabled(user.isEnabled())
//                .build();
//    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .map(this::toUserDetails)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User with name '%s' not found".formatted(username)));
    }

    private UserDetails toUserDetails(User user) {
        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(collectAuthorities(user.getRole().getRolename()))
                .disabled(!user.isEnabled())
                .build();
    }

    private List<GrantedAuthority> collectAuthorities(String role) {
        return roleRepository.findByRolename(role)
                .map(Role::getRolename)
                .map(priv -> (GrantedAuthority)new SimpleGrantedAuthority("PRIV_" + priv))
                .stream().toList();
    }

}
