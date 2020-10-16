package com.github.ollgei.boot.autoconfigure.segment.core;

import java.util.StringJoiner;

import org.springframework.context.ApplicationEvent;

/**
 * desc.
 * @author ollgei
 * @since 1.0.0
 */
@SuppressWarnings("serial")
public class NumberElementReloadEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5882774057868440823L;

    private Object event;

    private String eventDesc;

    public NumberElementReloadEvent(Object source, Object event, String eventDesc) {
        super(source);
        this.event = event;
        this.eventDesc = eventDesc;
    }

    public Object getEvent() {
        return this.event;
    }

    public String getEventDesc() {
        return this.eventDesc;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NumberElementReloadEvent.class.getSimpleName() + "[", "]")
                .add("event=" + event)
                .add("eventDesc='" + eventDesc + "'")
                .toString();
    }
}
