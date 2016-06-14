package com.pyrenty.akl.domain;

import com.pyrenty.akl.domain.enumeration.GameServerState;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "akl_game_server")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GameServer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "server_ip")
    private String server_ip;

    @Column(name = "rcon_ip")
    private String rcon_ip;

    @Column(name = "rcon_password")
    private String rcon_password;

    @Column(name = "server_port")
    private Integer server_port = 27015;

    @Column(name = "rcon_port")
    private Integer rcon_port = 27015;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private GameServerState state;
}
