package com.aeriscard.network;

public enum AerisCardAction {
    SWAP(0, "swap"), LEFT_CLICK(1, "left_click"), INSPECT(2, "inspect");

    private final int id;
    private final String triggerName;

    AerisCardAction(int id, String triggerName) { this.id = id; this.triggerName = triggerName; }
    public int id() { return id; }
    public String triggerName() { return triggerName; }

    public static AerisCardAction byId(int id) {
        for (AerisCardAction a : values()) if (a.id == id) return a;
        return null;
    }
}