package com.pyrenty.akl.web.rest;

import com.pyrenty.akl.Application;
import com.pyrenty.akl.domain.GameServer;
import com.pyrenty.akl.repository.GameServerRepository;
import com.pyrenty.akl.service.GameServerService;
import com.pyrenty.akl.web.rest.dto.GameServerDTO;
import com.pyrenty.akl.web.rest.mapper.GameServerMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.pyrenty.akl.domain.enumeration.GameServerState;

/**
 * Test class for the GameServerResource REST controller.
 *
 * @see GameServerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GameServerResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_SERVER_IP = "AAAAA";
    private static final String UPDATED_SERVER_IP = "BBBBB";
    private static final String DEFAULT_RCON_IP = "AAAAA";
    private static final String UPDATED_RCON_IP = "BBBBB";
    private static final String DEFAULT_RCON_PASSWORD = "AAAAA";
    private static final String UPDATED_RCON_PASSWORD = "BBBBB";

    private static final Integer DEFAULT_SERVER_PORT = 1;
    private static final Integer UPDATED_SERVER_PORT = 2;

    private static final Integer DEFAULT_RCON_PORT = 1;
    private static final Integer UPDATED_RCON_PORT = 2;


    private static final GameServerState DEFAULT_STATE = GameServerState.OFFLINE;
    private static final GameServerState UPDATED_STATE = GameServerState.IDLE;

    @Inject
    private GameServerRepository gameServerRepository;

    @Inject
    private GameServerMapper gameServerMapper;

    @Inject
    private GameServerService gameServerService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGameServerMockMvc;

    private GameServer gameServer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GameServerResource gameServerResource = new GameServerResource();
        ReflectionTestUtils.setField(gameServerResource, "gameServerService", gameServerService);
        this.restGameServerMockMvc = MockMvcBuilders.standaloneSetup(gameServerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        gameServer = new GameServer();
        gameServer.setName(DEFAULT_NAME);
        gameServer.setServer_ip(DEFAULT_SERVER_IP);
        gameServer.setRcon_ip(DEFAULT_RCON_IP);
        gameServer.setRcon_password(DEFAULT_RCON_PASSWORD);
        gameServer.setServer_port(DEFAULT_SERVER_PORT);
        gameServer.setRcon_port(DEFAULT_RCON_PORT);
        gameServer.setState(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void createGameServer() throws Exception {
        int databaseSizeBeforeCreate = gameServerRepository.findAll().size();

        // Create the GameServer
        GameServerDTO gameServerDTO = gameServerMapper.gameServerToGameServerDTO(gameServer);

        restGameServerMockMvc.perform(post("/api/gameServers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameServerDTO)))
                .andExpect(status().isCreated());

        // Validate the GameServer in the database
        List<GameServer> gameServers = gameServerRepository.findAll();
        assertThat(gameServers).hasSize(databaseSizeBeforeCreate + 1);
        GameServer testGameServer = gameServers.get(gameServers.size() - 1);
        assertThat(testGameServer.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGameServer.getServer_ip()).isEqualTo(DEFAULT_SERVER_IP);
        assertThat(testGameServer.getRcon_ip()).isEqualTo(DEFAULT_RCON_IP);
        assertThat(testGameServer.getRcon_password()).isEqualTo(DEFAULT_RCON_PASSWORD);
        assertThat(testGameServer.getServer_port()).isEqualTo(DEFAULT_SERVER_PORT);
        assertThat(testGameServer.getRcon_port()).isEqualTo(DEFAULT_RCON_PORT);
        assertThat(testGameServer.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    public void getAllGameServers() throws Exception {
        // Initialize the database
        gameServerRepository.saveAndFlush(gameServer);

        // Get all the gameServers
        restGameServerMockMvc.perform(get("/api/gameServers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(gameServer.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].server_ip").value(hasItem(DEFAULT_SERVER_IP.toString())))
                .andExpect(jsonPath("$.[*].rcon_ip").value(hasItem(DEFAULT_RCON_IP.toString())))
                .andExpect(jsonPath("$.[*].rcon_password").value(hasItem(DEFAULT_RCON_PASSWORD.toString())))
                .andExpect(jsonPath("$.[*].server_port").value(hasItem(DEFAULT_SERVER_PORT)))
                .andExpect(jsonPath("$.[*].rcon_port").value(hasItem(DEFAULT_RCON_PORT)))
                .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    public void getGameServer() throws Exception {
        // Initialize the database
        gameServerRepository.saveAndFlush(gameServer);

        // Get the gameServer
        restGameServerMockMvc.perform(get("/api/gameServers/{id}", gameServer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(gameServer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.server_ip").value(DEFAULT_SERVER_IP.toString()))
            .andExpect(jsonPath("$.rcon_ip").value(DEFAULT_RCON_IP.toString()))
            .andExpect(jsonPath("$.rcon_password").value(DEFAULT_RCON_PASSWORD.toString()))
            .andExpect(jsonPath("$.server_port").value(DEFAULT_SERVER_PORT))
            .andExpect(jsonPath("$.rcon_port").value(DEFAULT_RCON_PORT))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGameServer() throws Exception {
        // Get the gameServer
        restGameServerMockMvc.perform(get("/api/gameServers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGameServer() throws Exception {
        // Initialize the database
        gameServerRepository.saveAndFlush(gameServer);

		int databaseSizeBeforeUpdate = gameServerRepository.findAll().size();

        // Update the gameServer
        gameServer.setName(UPDATED_NAME);
        gameServer.setServer_ip(UPDATED_SERVER_IP);
        gameServer.setRcon_ip(UPDATED_RCON_IP);
        gameServer.setRcon_password(UPDATED_RCON_PASSWORD);
        gameServer.setServer_port(UPDATED_SERVER_PORT);
        gameServer.setRcon_port(UPDATED_RCON_PORT);
        gameServer.setState(UPDATED_STATE);
        GameServerDTO gameServerDTO = gameServerMapper.gameServerToGameServerDTO(gameServer);

        restGameServerMockMvc.perform(put("/api/gameServers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(gameServerDTO)))
                .andExpect(status().isOk());

        // Validate the GameServer in the database
        List<GameServer> gameServers = gameServerRepository.findAll();
        assertThat(gameServers).hasSize(databaseSizeBeforeUpdate);
        GameServer testGameServer = gameServers.get(gameServers.size() - 1);
        assertThat(testGameServer.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGameServer.getServer_ip()).isEqualTo(UPDATED_SERVER_IP);
        assertThat(testGameServer.getRcon_ip()).isEqualTo(UPDATED_RCON_IP);
        assertThat(testGameServer.getRcon_password()).isEqualTo(UPDATED_RCON_PASSWORD);
        assertThat(testGameServer.getServer_port()).isEqualTo(UPDATED_SERVER_PORT);
        assertThat(testGameServer.getRcon_port()).isEqualTo(UPDATED_RCON_PORT);
        assertThat(testGameServer.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    public void deleteGameServer() throws Exception {
        // Initialize the database
        gameServerRepository.saveAndFlush(gameServer);

		int databaseSizeBeforeDelete = gameServerRepository.findAll().size();

        // Get the gameServer
        restGameServerMockMvc.perform(delete("/api/gameServers/{id}", gameServer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GameServer> gameServers = gameServerRepository.findAll();
        assertThat(gameServers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
