package group1.unisim.achievement;

import group1.unisim.Service;

public class ServiceAchievement extends Achievement {
    private final Service service;
    private final int amountRequired;

    public ServiceAchievement(String id, String name, String description, Service service, int amountRequired) {
        super(id, name, description);
        this.service = service;
        this.amountRequired = amountRequired;
    }

    @Override
    public void onServiceValueChange(Service service, int newValue) {
        if (this.service == service && newValue >= this.amountRequired) {
            this.complete();
        }
    }
}
