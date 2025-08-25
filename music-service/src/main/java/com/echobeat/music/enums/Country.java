package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    KR("Korea"),
    JP("Japan"),
    GLOBAL("Global");

    private final String displayName;
}
