package eu.rex2go.chat2go.broadcast;

import lombok.Getter;

public class AutoBroadcast {

    @Getter
    private int id, interval;

    @Getter
    private String message;

    public AutoBroadcast(int id, int interval, String message) {
        this.id = id;
        this.interval = interval;
        this.message = message;
    }

}
