package it.webapp.ac_community_ita.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "best_lap")
    private String bestLap;

    @Column(name = "finish_time")
    private String finishTime;

 //   @Column(name = "elo_change")
  //  private Integer eloChange;

   // @Column(name = "incident_points")
  //  private Integer incidentPoints;
}