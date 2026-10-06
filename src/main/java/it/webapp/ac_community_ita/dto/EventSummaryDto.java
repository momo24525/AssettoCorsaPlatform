package it.webapp.ac_community_ita.dto;

import it.webapp.ac_community_ita.entity.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

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