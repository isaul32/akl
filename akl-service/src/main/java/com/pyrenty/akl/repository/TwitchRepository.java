package com.pyrenty.akl.repository;

import com.pyrenty.akl.dto.twitch.TwitchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Sauli on 14.5.2016.
 */
@Slf4j
@Repository
public class TwitchRepository {

    private RestTemplate restTemplate = new RestTemplate();

    @Value("${akl.twitch.name:}")
    private String twitchName;

    @Value("${akl.twitch.client-id:}")
    private String clientId;

    @Cacheable(value="twitch")
    public TwitchDto getTwitchState() {
        if (twitchName.isEmpty() || clientId.isEmpty()) {
            return null;
        }

        return restTemplate.getForObject("https://api.twitch.tv/kraken/streams/" + twitchName + "?client_id=" + clientId, TwitchDto.class);
    }
}
