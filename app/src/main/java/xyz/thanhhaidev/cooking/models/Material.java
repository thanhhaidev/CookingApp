package xyz.thanhhaidev.cooking.models;

/**
 * Created by ThanhHaiDev on 01-Jun-18.
 */
public class Material {
    private int id;
    private String name;
    private int protein;
    private int potassium;
    private int lipid;
    private int calo;
    private int cholesterol;
    private int sodium;
    private int weigh;

    public Material() {
    }

    public Material(int id, String name, int protein, int potassium, int lipid, int calo, int cholesterol, int sodium, int weigh) {
        this.id = id;
        this.name = name;
        this.protein = protein;
        this.potassium = potassium;
        this.lipid = lipid;
        this.calo = calo;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
        this.weigh = weigh;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getPotassium() {
        return potassium;
    }

    public void setPotassium(int potassium) {
        this.potassium = potassium;
    }

    public int getLipid() {
        return lipid;
    }

    public void setLipid(int lipid) {
        this.lipid = lipid;
    }

    public int getCalo() {
        return calo;
    }

    public void setCalo(int calo) {
        this.calo = calo;
    }

    public int getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(int cholesterol) {
        this.cholesterol = cholesterol;
    }

    public int getSodium() {
        return sodium;
    }

    public void setSodium(int sodium) {
        this.sodium = sodium;
    }

    public int getWeigh() {
        return weigh;
    }

    public void setWeigh(int weigh) {
        this.weigh = weigh;
    }
}
