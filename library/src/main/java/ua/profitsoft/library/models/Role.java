package ua.profitsoft.library.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "role")
@Transactional
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String rolename;

//    @OneToMany(cascade = CascadeType.ALL)
//    @JoinColumn(name = "id_user")
//    private List<User> users = new ArrayList<>();
}
