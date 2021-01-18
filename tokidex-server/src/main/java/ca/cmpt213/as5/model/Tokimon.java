package ca.cmpt213.as5.model;

public class Tokimon {

    private long tokimonId;
    private String name;
    private double weight;
    private double height;
    private String type;
    private int strength;
    private String color;

    public Tokimon(String name, double weight, double height, String type, int strength, String color) {
        this.name = name;
        this.weight = weight;
        this.height = height;
        this.type = type;
        this.strength = strength;
        this.color = color;
    }

    public void setTokimonId(long tokimonId) {
        this.tokimonId = tokimonId;
    }

    public long getTokimonId() {
        return tokimonId;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getHeight() {
        return height;
    }

    public String getType() {
        return type;
    }

    public int getStrength() {
        return strength;
    }

    public String getColor() {
        return color;
    }
}
