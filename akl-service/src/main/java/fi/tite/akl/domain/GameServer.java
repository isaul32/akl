package fi.tite.akl.domain;

import fi.tite.akl.domain.enumeration.GameServerState;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "akl_game_server")
public class GameServer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "server_ip")
    private String serverIp;

    @Column(name = "rcon_ip")
    private String rconIp;

    @Column(name = "rcon_password")
    private String rconPassword;

    @Column(name = "server_port")
    private Integer serverPort = 27015;

    @Column(name = "rcon_port")
    private Integer rconPort = 27015;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private GameServerState state;
}
