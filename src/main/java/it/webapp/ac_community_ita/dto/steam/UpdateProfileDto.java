package it.webapp.ac_community_ita.dto.steam;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateProfileDto {
    private String username;
    private String avatar;
}