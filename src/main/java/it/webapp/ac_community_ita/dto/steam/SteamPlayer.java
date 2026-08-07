package it.webapp.ac_community_ita.dto.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamPlayer {
    public String steamid;
    public String personaname;
    public String avatarfull;
}