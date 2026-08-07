package it.webapp.ac_community_ita.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class EventDto {

    @NotNull(message = "Seleziona una vettura")
    private Long carId;

    @NotNull(message = "Seleziona un tracciato")
    private Long trackId;


    private String title;

    @NotNull(message = "Inserisci una data/ora di partenza")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;

    @NotNull(message = "Inserisci un numero di giocatori")
    @Positive(message = "Il numero di giocatori deve essere positivo")
    private Integer maxPlayers;

    @NotNull(message = "Inserisci un tempo per la quali")
    @Positive(message = "La durata della quali deve essere positiva")
    private Integer qualyDuration; // minuti

    @NotNull(message = "Inserisci un tempo per la gara")
    @Positive(message = "La durata della gara deve essere positiva")
    private Integer raceDuration; // minuti

}