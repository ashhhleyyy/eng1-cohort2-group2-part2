package group1.unisim;

public class Thought {
    private String title;
    private String description;
    private int modification;

    public Thought() {
    }

    public Thought(String title, String description, int modification) {
        this.title = title;
        this.description = description;
        this.modification = modification;
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

    @Override
    public String toString() {
        return String.format("%s: %s",title,description);
    }
}
