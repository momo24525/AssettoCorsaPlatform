package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

// Aggiungi @JsonIgnoreProperties(ignoreUnknown = true) anche a CarEntryDto,
// DriverDto, ResultEntryDto e LapEntryDto.
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private List<CarEntryDto> cars;

    @JsonProperty("Result")
    private List<ResultEntryDto> result;

    @JsonProperty("Laps")
    private List<LapEntryDto> laps;

    @JsonProperty("Events")
    private List<Object> events;
}