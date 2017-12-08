package com.example.oi156f.bakeboss.components;

/**
 * Created by oi156f on 12/7/2017.
 *
 * Recipe object
 */

public class Recipe {

    private int id;
    private String name;
    private Ingredient[] ingredients;
    private Step[] steps;

    public Recipe(int id, String name, Ingredient[] ingredients, Step[] steps) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Recipe() {}

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

    public Ingredient[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredient[] ingredients) {
        this.ingredients = ingredients;
    }

    public Step[] getSteps() {
        return steps;
    }

    public void setSteps(Step[] steps) {
        this.steps = steps;
    }
}
