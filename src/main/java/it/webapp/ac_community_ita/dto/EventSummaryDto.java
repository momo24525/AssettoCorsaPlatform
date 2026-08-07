package it.webapp.ac_community_ita.dto;

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

    public int getPostiRimanenti() {
        return maxPlayers - registeredPlayers;
    }
}