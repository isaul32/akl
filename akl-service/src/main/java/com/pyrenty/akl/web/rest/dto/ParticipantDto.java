package com.pyrenty.akl.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonRootName(value = "participant")
public class ParticipantDto {
    private String name;
}
