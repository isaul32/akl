package fi.tite.akl.repository;

import fi.tite.akl.dto.twitch.TwitchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        List<String> names = Arrays.asList(twitchName.split("\\s*,\\s*"));
        for (String name : names) {
            TwitchDto twitchDto = restTemplate.getForObject("https://api.twitch.tv/kraken/streams/" + name + "?client_id=" + clientId, TwitchDto.class);
            if (Optional.ofNullable(twitchDto)
                    .map(TwitchDto::getStream)
                    .isPresent()) {
                return twitchDto;
            }
        }

        return null;
    }
}
