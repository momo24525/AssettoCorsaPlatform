package it.webapp.ac_community_ita.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "track")
@Getter
@Setter
@NoArgsConstructor // Genera il costruttore vuoto obbligatorio per JPA
@AllArgsConstructor // Genera il costruttore con tutti i campi
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "url_image", nullable = true, unique = true)
    private String urlImage;

}
