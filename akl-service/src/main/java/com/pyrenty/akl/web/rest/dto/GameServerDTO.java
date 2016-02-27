package com.pyrenty.akl.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;

import com.pyrenty.akl.domain.enumeration.GameServerState;

/**
 * A DTO for the GameServer entity.
 */
public class GameServerDTO implements Serializable {

    private Long id;

    private String name;

    private String server_ip;

    private String rcon_ip;

    private String rcon_password;

    private Integer server_port;

    private Integer rcon_port;

    private GameServerState state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServer_ip() {
        return server_ip;
    }

    public void setServer_ip(String server_ip) {
        this.server_ip = server_ip;
    }

    public String getRcon_ip() {
        return rcon_ip;
    }

    public void setRcon_ip(String rcon_ip) {
        this.rcon_ip = rcon_ip;
    }

    public String getRcon_password() {
        return rcon_password;
    }

    public void setRcon_password(String rcon_password) {
        this.rcon_password = rcon_password;
    }

    public Integer getServer_port() {
        return server_port;
    }

    public void setServer_port(Integer server_port) {
        this.server_port = server_port;
    }

    public Integer getRcon_port() {
        return rcon_port;
    }

    public void setRcon_port(Integer rcon_port) {
        this.rcon_port = rcon_port;
    }

    public GameServerState getState() {
        return state;
    }

    public void setState(GameServerState state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GameServerDTO gameServerDTO = (GameServerDTO) o;

        if ( ! Objects.equals(id, gameServerDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "GameServerDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", server_ip='" + server_ip + "'" +
            ", rcon_ip='" + rcon_ip + "'" +
            ", rcon_password='" + rcon_password + "'" +
            ", server_port='" + server_port + "'" +
            ", rcon_port='" + rcon_port + "'" +
            ", state='" + state + "'" +
            '}';
    }
}