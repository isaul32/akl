package fi.tite.akl.dto.twitch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class StreamDto {
    private String game;
    private String viewers;
    private ChannelDto channel;
}
