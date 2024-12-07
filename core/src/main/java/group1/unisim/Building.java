package group1.unisim;

public class Building {

    private String name;
    private Service[] servicesProvided;
    private int constructionTime;
    private int size;
    private String texture;

    public Building() {
    }

    public Building(String name, Service[] servicesProvided, int constructionTime, int size, String sprite) {
        this.name = name;
        this.servicesProvided = servicesProvided;
        this.constructionTime = constructionTime;
        this.size = size;
        this.texture = sprite;
    }

    public String getName() {
        return name;
    }

    public Service[] getServicesProvided() {
        return servicesProvided;
    }

    public int getConstructionTime() {
        return constructionTime;
    }

    public int getSize() {
        return size;
    }

    public String getTexture() {
        return texture;
    }
}
