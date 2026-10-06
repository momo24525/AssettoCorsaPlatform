package it.webapp.ac_community_ita.service.steam;

import it.webapp.ac_community_ita.dto.steam.SteamPlayer;
import org.springframework.stereotype.Service;

@Service
public class SteamProfileService {

    // TODO: sostituire con la chiamata vera a GetPlayerSummaries
    // quando la Steam API key sarà disponibile (account sbloccato con $5)
    public SteamPlayer fetchProfile(String steamId) {
        SteamPlayer fakePlayer = new SteamPlayer();
        fakePlayer.steamid = steamId; // questo è vero, arriva dal login reale
        fakePlayer.personaname = "Anonymous User";
        fakePlayer.avatarfull = "https://avatars.steamstatic.com/fef49e7fa7e1997310d705b2a6158ff8dc1cdfeb_full.jpg";

        return fakePlayer;
    }
}

/******VERSIONE CON API KEY, DA IMPLEMENTARE QUANDO AVRO' SPESO I 5 DOLLARI***************+
@Service
public class SteamProfileService {

    @Value("${steam.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public SteamPlayer fetchProfile(String steamId) {
        String url = UriComponentsBuilder
                .fromUriString("https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/")
                .queryParam("key", apiKey)
                .queryParam("steamids", steamId)
                .build().toUriString();

        SteamApiResponse response = restTemplate.getForObject(url, SteamApiResponse.class);

        if (response == null || response.response.players.isEmpty()) {
            throw new IllegalStateException("Profilo Steam non trovato per id: " + steamId);
        }
        return response.response.players.get(0);
    }
}  */