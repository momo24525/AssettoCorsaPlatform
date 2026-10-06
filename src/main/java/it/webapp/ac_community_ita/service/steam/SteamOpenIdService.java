package it.webapp.ac_community_ita.service.steam;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class SteamOpenIdService {

    private static final String STEAM_LOGIN_URL = "https://steamcommunity.com/openid/login";
    private static final Pattern CLAIMED_ID_PATTERN =
            Pattern.compile("^https://steamcommunity\\.com/openid/id/(\\d+)$");

    @Value("${steam.realm}")
    private String realm;

    @Value("${steam.return-url}")
    private String returnUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String buildLoginUrl() {
        return UriComponentsBuilder.fromUriString(STEAM_LOGIN_URL)
                .queryParam("openid.ns", "http://specs.openid.net/auth/2.0")
                .queryParam("openid.mode", "checkid_setup")
                .queryParam("openid.return_to", returnUrl)
                .queryParam("openid.realm", realm)
                .queryParam("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select")
                .queryParam("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select")
                .build().toUriString();
    }

    public Optional<String> validateAndExtractSteamId(HttpServletRequest request) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        request.getParameterMap().forEach((key, values) -> params.put(key, Arrays.asList(values)));
        params.set("openid.mode", "check_authentication");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

        String verifyResponse = restTemplate.postForObject(STEAM_LOGIN_URL, entity, String.class);
        if (verifyResponse == null || !verifyResponse.contains("is_valid:true")) {
            return Optional.empty();
        }

        String claimedId = request.getParameter("openid.claimed_id");
        if (claimedId == null) return Optional.empty();

        var matcher = CLAIMED_ID_PATTERN.matcher(claimedId);
        if (!matcher.matches()) {
            return Optional.empty();
        }

        return Optional.of(matcher.group(1));
    }
}