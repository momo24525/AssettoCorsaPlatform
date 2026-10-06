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

