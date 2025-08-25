package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleType {
    MAIN("Main Artist"),
    FEATURED("Featured"),
    PRODUCER("Producer"),
    COMPOSER("Composer"),
    LYRICIST("Lyricist"),
    COLLABORATION("Collaboration");

    private final String displayName;
}
