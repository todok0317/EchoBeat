package com.echobeat.music.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UpdateCycle {
    REAL_TIME(300),      // 5분
    HOURLY(3600),        // 1시간
    DAILY(86400),        // 24시간
    WEEKLY(604800);      // 7일

    private final int intervalSeconds;
}
