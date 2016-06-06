package com.pyrenty.akl.web.rest.dto;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class LoggerDTO {
    private String name;
    private String level;

    public LoggerDTO(Logger logger) {
        this.name = logger.getName();
        this.level = logger.getEffectiveLevel().toString();
    }

    @JsonCreator
    public LoggerDTO() {
    }
}
