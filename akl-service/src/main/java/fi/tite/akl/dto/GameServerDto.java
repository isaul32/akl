package fi.tite.akl.dto;

import fi.tite.akl.domain.enumeration.GameServerState;
import lombok.Data;

@Data
public class GameServerDto {
    private Long id;
    private String name;
    private String serverIp;
    private String rconIp;
    private String rconPassword;
    private Integer serverPort;
    private Integer rconPort;
    private GameServerState state;
}
