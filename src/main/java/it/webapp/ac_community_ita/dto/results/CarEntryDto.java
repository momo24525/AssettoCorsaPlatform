package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarEntryDto {

    @JsonProperty("CarId")
    private Long id;

    @JsonProperty("Driver")
    private DriverDto driver;

    @JsonProperty("Model")
    private String model;

    @JsonProperty("Skin")
    private String skin;

    @JsonProperty("BallastKG")
    private Integer ballastKg;

    @JsonProperty("Restrictor")
    private Integer restrictor;


}