package group1.unisim;

import group1.unisim.Building.Service;

public class Event {
    private final String description;
    private float duration;
    private final Service service;
    private final int requirement;
    private final Main main;


    public Event(String description, int duration, Service service, int requirement, Main main) {
        this.description = description;
        this.duration = duration;
        this.service = service;
        this.requirement = requirement;
        this.main = main;
    }

    public void update(float deltaTime) {
        if (duration <= 0) return;
        duration -= deltaTime;
        if (duration <= 0) this.end();
    }

    public void end() {
        main.removeEvent(this);
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
