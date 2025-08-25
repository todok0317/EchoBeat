package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlbumType {
    SINGLE("Single"),
    EP("EP"),
    ALBUM("Full Album"),
    COMPILATION("Compilation"),
    SOUNDTRACK("Soundtrack"),
    REMIX("Remix Album");

    private final String displayName;
}
