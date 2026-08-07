package it.webapp.ac_community_ita.service;

import it.webapp.ac_community_ita.dto.steam.SteamPlayer;
import it.webapp.ac_community_ita.dto.steam.UpdateProfileDto;
import it.webapp.ac_community_ita.entity.Role;
import it.webapp.ac_community_ita.entity.User;
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
                .map(existingUser -> updateProfileData(existingUser, profile))
                .orElseGet(() -> createNewUser(profile));
    }

    private User updateProfileData(User user, SteamPlayer profile) {
        user.setAvatarUrl(profile.avatarfull);
        return userRepository.save(user);
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

    public User updateUserProfile(String steamId, UpdateProfileDto updateProfileDto) {
        // 1. Cercare l'utente o lanciare un'eccezione se non esiste a DB
        User user = userRepository.findBySteamId(steamId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato per steamId: " + steamId));

        // 2. Aggiornare solo i campi presenti nel DTO
        if (updateProfileDto.getUsername() != null && !updateProfileDto.getUsername().isBlank()) {
            user.setUsername(updateProfileDto.getUsername());
        }

        if (updateProfileDto.getAvatar() != null && !updateProfileDto.getAvatar().isBlank()) {
            user.setAvatarUrl(updateProfileDto.getAvatar());
        }

        // 3. Salvare l'utente aggiornato
        return userRepository.save(user);
    }

    public UpdateProfileDto getUpdateProfileDto(String steamId) {
        User user = userRepository.findBySteamId(steamId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato per steamId: " + steamId));

        return new UpdateProfileDto(user.getUsername(), user.getAvatarUrl());
    }

}