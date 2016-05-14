package com.pyrenty.akl.repository;

import com.pyrenty.akl.domain.twitch.Twitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Sauli on 14.5.2016.
 */
@Repository
public class TwitchRepository {
    private final Logger log = LoggerFactory.getLogger(TwitchRepository.class);
    private RestTemplate restTemplate = new RestTemplate();

    @Value("${akl.twitch.name}")
    private String twitchName;

    @Value("${akl.twitch.client-id}")
    private String clientId;

    @Cacheable(value="twitch")
    public Twitch getTwitchState() {
        return restTemplate.getForObject("https://api.twitch.tv/kraken/streams/" + twitchName + "?client_id=" + clientId, Twitch.class);
    }
}
