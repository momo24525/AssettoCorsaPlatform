package it.webapp.ac_community_ita.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pending_result")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "filename", nullable = false)
    private String filename;

    @Column(name = "track_name", nullable = false)
    private String trackName;

    @Column(name = "raw_json", columnDefinition = "TEXT", nullable = false)
    private String rawJson;

    @Column(name = "received_at", nullable = false)
    private LocalDateTime receivedAt;
}