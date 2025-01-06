package group1.unisim.thought;

import group1.unisim.building.Service;

public class Thought {
    // added service and diff fields.
    private String title;
    private String description;
    private int modification;
    private Service service;
    private int diff;

    public Thought() {
    }

    // refactored to not use this. instead of _name
    public Thought(String title, String description, int modification) {
        this.title = title;
        this.description = description;
        this.modification = modification;
    }

    // ADDED a new constructor.
    public Thought(String title, String description, int modification, Service service, int diff) {
        this(title, description, modification);
        this.service = service;
        this.diff = diff;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getModification() {
        return modification;
    }

    // ADDED toString, getDiff and getService
    @Override
    public String toString() {
        return String.format("%s: %s", title, description);
    }

    public int getDiff() {
        return diff;
    }

    public Service getService() {
        return service;
    }
}
