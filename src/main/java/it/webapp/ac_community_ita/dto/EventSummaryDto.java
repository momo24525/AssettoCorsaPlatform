package it.webapp.ac_community_ita.dto;

import it.webapp.ac_community_ita.entity.EventStatus;
import lombok.Getter;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class EventSummaryDto {

    private Long id;
    private String title;
    private String trackName;
    private String carName;
    private LocalDateTime startTime;
    private int registeredPlayers;
    private int maxPlayers;
    private String trackImageUrl;
    private boolean registered;
    private EventStatus eventStatus;

    public int getPostiRimanenti() {
        return maxPlayers - registeredPlayers;
    }
}