package it.webapp.ac_community_ita.entity.results;

import it.webapp.ac_community_ita.entity.event.Event;
import it.webapp.ac_community_ita.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "position")
    private Integer position;

    @Column(name = "laps")
    private Integer laps;

    @Column(name = "best_lap_ms")
    private Integer bestLapMs;

    @Column(name = "finish_time_ms")
    private Integer finishTimeMs;

 //   @Column(name = "elo_change")
  //  private Integer eloChange;

   // @Column(name = "incident_points")
  //  private Integer incidentPoints;
}