package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ChartSource {
    APPLE_MUSIC("Apple Music"),
    MELON("Melon"),
    BILLBOARD_JAPAN("Billboard Japan"),
    ORICON("Oricon"),
    SPOTIFY("Spotify");

    private final String displayName;
}