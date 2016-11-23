package com.iktwo.switcha.model;

/**
 * Switch with on / off codes.
 */

public class Switch {
    public String name;
    public String onCode;
    public String offCode;

    public Switch() {

    }

    public Switch(String name, String onCode, String offCode) {
        this.name = name;
        this.onCode = onCode;
        this.offCode = offCode;
    }
}
