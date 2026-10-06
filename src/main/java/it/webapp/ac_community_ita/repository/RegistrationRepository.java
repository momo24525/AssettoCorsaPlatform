package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.registration.Registration;
import it.webapp.ac_community_ita.entity.registration.RegistrationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    @Query("SELECT COUNT(r) FROM Registration r WHERE r.event.id = :eventId AND r.status = :status")
    long countByEventIdAndStatus(@Param("eventId") Long eventId,
                                 @Param("status") RegistrationStatus status);

    Optional<Registration> findByUserIdAndEventId(Long userId, Long eventId);
}

