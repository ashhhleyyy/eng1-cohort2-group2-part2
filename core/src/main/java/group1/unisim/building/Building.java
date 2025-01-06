package group1.unisim.building;

public class Building {
    private String name;
    private Service[] servicesProvided;
    private int constructionTime;
    private int size;
    // CHANGED: rename sprite to texture
    private String texture;

    public Building() {
    }

    public Building(String name, Service[] servicesProvided, int constructionTime, int size, String texture) {
        this.name = name;
        this.servicesProvided = servicesProvided;
        this.constructionTime = constructionTime;
        this.size = size;
        this.texture = texture;
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
