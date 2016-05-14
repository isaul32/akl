package com.pyrenty.akl.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.pyrenty.akl.domain.enumeration.GameServerState;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class GameServerDTO implements Serializable {
    private Long id;
    private String name;
    private String server_ip;
    private String rcon_ip;
    private String rcon_password;
    private Integer server_port;
    private Integer rcon_port;
    private GameServerState state;
}
