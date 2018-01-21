package com.example.oi156f.bakeboss;

import android.os.AsyncTask;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;


/**
 * Created by oi156f on 1/17/2018.
 */

public class FetchRecipesTask extends AsyncTask<Void, Void, Recipe[]> {

    private OnTaskCompleted onTaskCompleted;

    public FetchRecipesTask(OnTaskCompleted taskCompleted) {
        onTaskCompleted = taskCompleted;
    }

    @Override
    protected Recipe[] doInBackground(Void... voids) {
        try {
            String recipeJson = RecipeUtils.getResponseFromHttpUrl(RecipeUtils.buildUrl());
            Recipe[] recipes = RecipeUtils.getRecipesFromJson(recipeJson);
            return recipes;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Recipe[] recipes) {
        for (int i = 0; i < recipes.length; i++) {
            if (recipes[i].getImage().isEmpty())
                recipes[i].setImage(RecipeUtils.imageURLS[i]);
        }
        onTaskCompleted.onTaskCompleted(recipes);
    }

    public interface OnTaskCompleted {
        void onTaskCompleted(Recipe[] recipes);
    }
}
