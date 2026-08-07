package it.webapp.ac_community_ita.dto.steam;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateProfileDto {
    public String username;
    public String avatar;
}