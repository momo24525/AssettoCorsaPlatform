package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findBySteamId(String steamId);
}