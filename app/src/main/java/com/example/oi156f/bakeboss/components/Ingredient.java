package com.example.oi156f.bakeboss.components;

/**
 * Created by oi156f on 12/7/2017.
 *
 * Ingredient object
 */

public class Ingredient {

    private String name;
    private double quantity;
    private String measure;

    public Ingredient(String name, double quantity, String measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }
}
