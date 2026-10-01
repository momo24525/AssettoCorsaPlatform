package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LapEntryDto {

    @JsonProperty("DriverName")
    private String driverName;

    @JsonProperty("DriverGuid")
    private String driverGuid;

    @JsonProperty("CarId")
    private Long carId;

    @JsonProperty("CarModel")
    private String carModel;

    @JsonProperty("Timestamp")
    private Long timestamp;

    @JsonProperty("LapTime")
    private Integer lapTime;

    @JsonProperty("Sectors")
    private List<Integer> sectors;

    @JsonProperty("Cuts")
    private Integer cuts;

    @JsonProperty("BallastKG")
    private Integer ballastKg;

    @JsonProperty("Tyre")
    private String tyre;

    @JsonProperty("Restrictor")
    private Integer restrictor;
}