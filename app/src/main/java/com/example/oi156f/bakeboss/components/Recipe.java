package com.example.oi156f.bakeboss.components;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by oi156f on 12/7/2017.
 *
 * Recipe object
 */

public class Recipe implements Parcelable {

    private int id;
    private String name;
    private Ingredient[] ingredients;
    private Step[] steps;
    private int servings;
    private String image;

    public Recipe(int id, String name, Ingredient[] ingredients, Step[] steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public Recipe() {}

    private Recipe(Parcel source) {
        id = source.readInt();
        name = source.readString();
        ingredients = source.createTypedArray(Ingredient.CREATOR);
        steps = source.createTypedArray(Step.CREATOR);
        servings = source.readInt();
        image = source.readString();

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

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ArrayList<String> getIngredientsList() {
        ArrayList<String> ingredientsList = new ArrayList<>();
        for (Ingredient ing : ingredients) {
            String ingredientString = Double.toString(ing.getQuantity()) + " " + ing.getMeasure() + " " + ing.getName();
            ingredientsList.add(ingredientString);
        }
        return ingredientsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeTypedArray(ingredients, 0);
        parcel.writeTypedArray(steps, 0);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };
}
