package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ArtistType {
    SOLO("Solo"),
    GROUP("Group"),
    BAND("Band"),
    COLLABORATION("Collaboration");

    private final String displayName;
}
