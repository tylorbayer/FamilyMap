package fmc.model;

import java.io.Serializable;

public class Filters implements Serializable {
    private String eventType;
    private boolean show;

    public Filters(String eventType) {
        this.eventType = eventType;
        show = false;
    }

    public String getEventType() {
        return eventType;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }
}
