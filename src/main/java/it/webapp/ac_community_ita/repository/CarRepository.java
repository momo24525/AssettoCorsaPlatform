package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByName(String name);
}