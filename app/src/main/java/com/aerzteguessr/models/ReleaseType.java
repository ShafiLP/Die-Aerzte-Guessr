package com.aerzteguessr.models;

public enum ReleaseType {
    ALBUM("Album"),
    SINGLE("Single"),
    EP("EP"),
    COMPILATION("Compilation"),
    LIVE("Live"),
    UNKNOWN("Unknown");

    private final String value;

    ReleaseType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
