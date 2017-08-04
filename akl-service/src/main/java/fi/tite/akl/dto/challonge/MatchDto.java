package fi.tite.akl.dto.challonge;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonRootName(value = "match")
public class MatchDto {
    private Long tournament_url;
    private String subdomain;
    private String state;
    private Long participant_id;
}
