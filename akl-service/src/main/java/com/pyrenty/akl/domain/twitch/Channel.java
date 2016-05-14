package com.pyrenty.akl.domain.twitch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Channel {
    private String name;
    private String url;
    private String status;
}
