package it.webapp.ac_community_ita.dto.steam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SteamApiResponse {
    public Response response;

    public static class Response {
        public List<SteamPlayer> players;
    }
}