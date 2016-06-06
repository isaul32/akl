package com.pyrenty.akl.web.rest.dto;

import com.pyrenty.akl.domain.enumeration.GameServerState;
import lombok.Data;

@Data
public class GameServerDTO {
    private Long id;
    private String name;
    private String server_ip;
    private String rcon_ip;
    private String rcon_password;
    private Integer server_port;
    private Integer rcon_port;
    private GameServerState state;
}
