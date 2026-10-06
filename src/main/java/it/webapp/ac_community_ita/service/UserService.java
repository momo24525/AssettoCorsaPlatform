package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.steam.SteamPlayer;
import it.webapp.ac_community_ita.dto.steam.UpdateProfileDto;
import it.webapp.ac_community_ita.entity.user.Role;
import it.webapp.ac_community_ita.entity.user.User;
import it.webapp.ac_community_ita.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findOrCreateFromSteam(SteamPlayer profile) {
        return userRepository.findBySteamId(profile.steamid)
                .orElseGet(() -> createNewUser(profile));
    }


    private User createNewUser(SteamPlayer profile) {
        User user = new User();
        user.setSteamId(profile.steamid);
        user.setUsername(profile.personaname);
        user.setAvatarUrl(profile.avatarfull);
        user.setRole(Role.USER); // adatta al nome esatto del tuo enum, se diverso
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User updateUserProfile(String steamId, UpdateProfileDto dto) {

        User user = userRepository.findBySteamId(steamId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Utente non trovato"));

        if (dto.getUsername() != null && !dto.getUsername().isBlank()) {

            boolean usernameTaken = userRepository
                    .existsByUsername(dto.getUsername());

            if (usernameTaken &&
                    !user.getUsername().equals(dto.getUsername())) {

                throw new IllegalArgumentException(
                        "Username già in uso"
                );
            }
            user.setUsername(dto.getUsername());
            user.setCompleted(true);
        }

        if (dto.getAvatar() != null && !dto.getAvatar().isBlank()) {
            user.setAvatarUrl(dto.getAvatar());
        }

        return userRepository.save(user);
    }

    public UpdateProfileDto getUpdateProfileDto(String steamId) {
        User user = userRepository.findBySteamId(steamId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato per steamId: " + steamId));

        return new UpdateProfileDto(user.getUsername(), user.getAvatarUrl());
    }

    public User findbySteamId (String steamId) {
        return userRepository.findBySteamId(steamId)
                .orElseThrow(() -> new RuntimeException("User not found for steamId: " + steamId));
    }

}