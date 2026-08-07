package it.webapp.ac_community_ita.dto;

import it.webapp.ac_community_ita.entity.CarClass;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackDto {

    @NotBlank(message = "Il nome è obbligatorio")
    private String name;

}