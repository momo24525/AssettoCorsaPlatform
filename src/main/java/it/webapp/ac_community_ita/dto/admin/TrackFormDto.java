package it.webapp.ac_community_ita.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackFormDto {

    @NotBlank(message = "Il nome è obbligatorio")
    private String name;

}