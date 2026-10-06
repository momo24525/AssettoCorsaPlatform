package it.webapp.ac_community_ita.dto.results;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Anteprima di un PendingResult per la pagina admin: cosa verra' creato se confermi.
 * Nel template chiama gli accessor come metodi: preview.filename(), row.username(), ...
 */
public record PendingResultPreviewDto(
        Long id,
        String filename,
        String track,
        LocalDateTime receivedAt,
        Integer raceLaps,
        Long suggestedEventId,
        List<Row> rows) {

    /** username == null significa: nessun utente del sito con quello SteamID (verra' saltato). */
    public record Row(int position,
                      String driverName,
                      String guid,
                      String username,
                      int laps,
                      String bestLap,
                      String totalTime) {
    }
}