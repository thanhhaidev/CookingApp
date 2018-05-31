package xyz.thanhhaidev.cooking.models;

/**
 * Created by ThanhHaiDev on 31-May-18.
 */
public class Food {
    private String name;
    private String image;
    private int id_food;
    private String content;

    public Food() {
    }

    public Food(String name, String image, int id_food, String content) {
        this.name = name;
        this.image = image;
        this.id_food = id_food;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_food() {
        return id_food;
    }

    public void setId_food(int id_food) {
        this.id_food = id_food;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
