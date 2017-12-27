package com.example.oi156f.bakeboss.components;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oi156f on 12/27/2017.
 *
 * Ingredient object
 */

public class Ingredient implements Parcelable {

    private String name;
    private double quantity;
    private String measure;

    public Ingredient(String name, double quantity, String measure) {
        this.name = name;
        this.quantity = quantity;
        this.measure = measure;
    }

    public Ingredient() {}

    private Ingredient(Parcel source) {
        name = source.readString();
        quantity = source.readDouble();
        measure = source.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(quantity);
        parcel.writeString(measure);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }
    };
}
