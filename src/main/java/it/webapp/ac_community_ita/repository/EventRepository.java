package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.Event;
import it.webapp.ac_community_ita.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
           SELECT e FROM Event e
           WHERE e.startTime > :now
             AND e.status = :status
           ORDER BY e.startTime ASC
           """)
    List<Event> findUpcomingEvents(@Param("now") LocalDateTime now,
                                           @Param("status") EventStatus status,
                                           Pageable pageable);

    Optional<Event> findFirstByStatusOrderByStartTimeAsc(EventStatus status);

    List<Event> findByStatusAndStartTimeLessThanEqual(EventStatus status, LocalDateTime threshold);


}
