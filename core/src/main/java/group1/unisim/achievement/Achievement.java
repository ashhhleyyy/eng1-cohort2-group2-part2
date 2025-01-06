package group1.unisim.achievement;

import group1.unisim.building.Service;

// ADDED: implement achievements
public abstract class Achievement {
    private final String id;
    private final String name;
    private final String description;
    private boolean achieved;

    public Achievement(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void onServiceValueChange(Service service, int newValue) {
    }

    public void onSatisfactionChange(float newSatisfaction) {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isComplete() {
        return achieved;
    }

    protected void complete() {
        if (this.achieved) {
            System.err.println("Achievement `" + this.name + "` has already been achieved!");
            return;
        }
        this.achieved = true;
    }

    @Override
    public String toString() {
        return this.getName() + ": " + this.getDescription();
    }
}
