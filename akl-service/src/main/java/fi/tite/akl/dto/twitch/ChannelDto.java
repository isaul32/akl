package fi.tite.akl.dto.twitch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ChannelDto {
    private String name;
    private String url;
    private String status;
}
