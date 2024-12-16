package group1.unisim.achievement;

public class SatisfactionAchievement extends Achievement {
    private final float satisfactionRequirement;
    private final boolean greater;

    public SatisfactionAchievement(String id, String name, String description, float satisfactionRequirement, boolean greater) {
        super(id, name, description);
        this.satisfactionRequirement = satisfactionRequirement;
        this.greater = greater;
    }

    @Override
    public void onSatisfactionChange(float newSatisfaction) {
        if (greater) {
            if (newSatisfaction >= this.satisfactionRequirement) {
                this.complete();
            }
        } else {
            if (newSatisfaction <= this.satisfactionRequirement) {
                this.complete();
            }
        }
        super.onSatisfactionChange(newSatisfaction);
    }
}
