package it.webapp.ac_community_ita.dto.admin;

import it.webapp.ac_community_ita.entity.car.CarClass;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarFormDto {

    @NotBlank(message = "Il nome è obbligatorio")
    private String name;

    @NotNull(message = "La classe è obbligatoria")
    private CarClass carClass;
}