package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.PendingResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PendingResultRepository extends JpaRepository<PendingResult, Long> {

    List<PendingResult> findAllByOrderByReceivedAtAsc();
    boolean existsByFilename(String filename);
}