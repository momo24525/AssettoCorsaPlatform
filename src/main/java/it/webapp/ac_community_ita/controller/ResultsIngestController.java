package it.webapp.ac_community_ita.controller;

import it.webapp.ac_community_ita.service.PendingResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class ResultsIngestController {

    private final PendingResultService pendingResultService;

    /**
     * Risponde 200 sia quando salva sia quando ignora (non e' una gara, o file gia' ricevuto):
     * lo script Python considera "riuscito" solo un 2xx e sposta il file in sent/.
     * Risponde 400 solo se il JSON non e' valido.
     */
    @PostMapping("/admin/results/incoming")
    public ResponseEntity<String> incoming(@RequestParam String filename,
                                           @RequestBody byte[] body) {
        // Decodifica esplicita in UTF-8, per non dipendere dal charset del Content-Type
        String rawJson = new String(body, StandardCharsets.UTF_8);

        try {
            boolean saved = pendingResultService.saveIfRace(filename, rawJson);
            return ResponseEntity.ok(saved ? "saved" : "ignored");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("invalid json");
        }
    }
}