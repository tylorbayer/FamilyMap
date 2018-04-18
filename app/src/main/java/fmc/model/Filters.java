package fmc.model;

import java.io.Serializable;

public class Filters implements Serializable, Cloneable {
    private String eventType;
    private boolean show = true;

    public Filters(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public Filters clone() throws CloneNotSupportedException {
        return (Filters) super.clone();
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
