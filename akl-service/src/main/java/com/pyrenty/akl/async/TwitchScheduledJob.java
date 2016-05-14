package com.pyrenty.akl.async;

import com.pyrenty.akl.domain.twitch.Twitch;
import com.pyrenty.akl.repository.TwitchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class TwitchScheduledJob {
    private final Logger log = LoggerFactory.getLogger(TwitchScheduledJob.class);
    private final static long POLLING_RATE = 60000;

    @Inject
    private TwitchRepository twitchRepository;

    @Scheduled(fixedRate=POLLING_RATE)
    public void printMessage() {
        Twitch twitch = twitchRepository.getTwitchState();

        // Channel is online if stream is not null
        if (twitch.getStream() != null) {
            // todo: Send websocket brodcast to clients
            log.debug(twitch.toString());
        }
    }
}
