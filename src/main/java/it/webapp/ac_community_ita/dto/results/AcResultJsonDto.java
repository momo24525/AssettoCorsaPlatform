package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AcResultJsonDto {

    @JsonProperty("TrackName")
    private String trackName;

    @JsonProperty("TrackConfig")
    private String trackConfig;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("DurationSecs")
    private Integer durationSecs;

    @JsonProperty("RaceLaps")
    private Integer raceLaps;

    @JsonProperty("Cars")
    private List<Object> cars;

    @JsonProperty("Result")
    private List<Object> result;

    @JsonProperty("Laps")
    private List<Object> laps;

    @JsonProperty("Events")
    private List<Object> events;
}