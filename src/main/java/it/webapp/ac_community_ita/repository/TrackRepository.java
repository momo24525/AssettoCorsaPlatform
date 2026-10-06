package it.webapp.ac_community_ita.repository;

import it.webapp.ac_community_ita.entity.track.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
    boolean existsByName(String name);
}