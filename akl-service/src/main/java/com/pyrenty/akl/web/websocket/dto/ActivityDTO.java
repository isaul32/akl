package com.pyrenty.akl.web.websocket.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO for storing a user's activity.
 */
@Getter
@Setter
@ToString
public class ActivityDTO {
    private String sessionId;
    private String userLogin;
    private String ipAddress;
    private String page;
    private String time;
}
