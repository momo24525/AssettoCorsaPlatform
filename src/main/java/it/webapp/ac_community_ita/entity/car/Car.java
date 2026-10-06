package it.webapp.ac_community_ita.entity.car;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "car")
@Getter
@Setter
@NoArgsConstructor // Genera il costruttore vuoto obbligatorio per JPA
@AllArgsConstructor // Genera il costruttore con tutti i campi
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "car_class", nullable = true)
    private CarClass carClass;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
