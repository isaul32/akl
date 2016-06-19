package com.pyrenty.akl.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@JsonRootName(value = "tournament")
public class TournamentDto {
    @Length(max = 60)
    private String name;
    private String tournament_type;
    private String subdomain;
    private String url;
    private String open_signup = "false";
    private String ranked_by = "match wins";
}
