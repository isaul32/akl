package com.pyrenty.akl.async;

import com.pyrenty.akl.dto.twitch.TwitchDto;
import com.pyrenty.akl.repository.TwitchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Slf4j
@Component
public class TwitchScheduledJob {

    private final static long POLLING_RATE = 60000;

    @Inject
    private TwitchRepository twitchRepository;

    @Scheduled(fixedRate=POLLING_RATE)
    public void printMessage() {
        TwitchDto twitch = twitchRepository.getTwitchState();

        // ChannelDto is online if stream is not null
        if (twitch != null && twitch.getStream() != null) {
            // todo: Send websocket brodcast to clients
            log.debug(twitch.toString());
        }
    }
}
