package it.webapp.ac_community_ita.entity.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor // Genera il costruttore vuoto obbligatorio per JPA
@AllArgsConstructor // Genera il costruttore con tutti i campi
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "steam_id", nullable = false, unique = true)
    private String steamId;

    @Column(name = "avatar_url", nullable = true, unique = true)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;


}
