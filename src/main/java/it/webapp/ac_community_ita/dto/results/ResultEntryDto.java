package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ResultEntryDto {

    @JsonProperty("DriverName")
    private String driverName;

    @JsonProperty("DriverGuid")
    private String driverGuid;

    @JsonProperty("CarId")
    private Long carId;

    @JsonProperty("CarModel")
    private String carModel;

    @JsonProperty("BestLap")
    private Integer bestLap;

    @JsonProperty("TotalTime")
    private Integer totalTime;

    @JsonProperty("BallastKG")
    private Integer ballastKg;

    @JsonProperty("Restrictor")
    private Integer restrictor;


}