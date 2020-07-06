package eu.rex2go.chat2go.broadcast;

import lombok.Getter;
import lombok.Setter;

public class AutoBroadcast {

    @Getter
    private int id, interval, offset;

    @Getter
    @Setter
    private int remainingTime;

    @Getter
    private String message;

    public AutoBroadcast(int id, int interval, int offset, String message) {
        this.id = id;
        this.interval = interval;
        this.message = message;
        this.remainingTime = interval + offset;
        this.offset = offset;
    }

    public void resetRemainingTime() {
        remainingTime = interval;
    }

}
