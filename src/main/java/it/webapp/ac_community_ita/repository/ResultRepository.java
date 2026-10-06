package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.results.Result;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResultRepository extends JpaRepository<Result, Long> {

    boolean existsByEventId(Long eventId);
}