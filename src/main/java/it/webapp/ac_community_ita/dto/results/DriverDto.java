package it.webapp.ac_community_ita.dto.results;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DriverDto {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Team")
    private String team;

    @JsonProperty("Nation")
    private String nation;

    @JsonProperty("Guid")
    private String guid;

    @JsonProperty("GuidsList")
    private List<Object> guidList;


}