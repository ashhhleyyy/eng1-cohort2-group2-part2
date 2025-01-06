package group1.unisim.events;

import group1.unisim.building.Service;

// ADDED: empty methods implemented, along with new getters/fields
public class Event {
    private final String description;
    private final Service service;
    private final int requirement;
    private final EventManager eventManager;
    private float duration;


    public Event(String description, int duration, Service service, int requirement, EventManager eventManager) {
        this.description = description;
        this.duration = duration;
        this.service = service;
        this.requirement = requirement;
        this.eventManager = eventManager;
    }

    public void update(float deltaTime) {
        if (duration <= 0) return;
        duration -= deltaTime;
        if (duration <= 0) this.end();
    }

    public void end() {
        eventManager.removeEvent(this);
    }

    public float getDuration() {
        return duration;
    }

    public Service getService() {
        return service;
    }

    public int getRequirement() {
        return requirement;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return duration > 0;
    }
}
