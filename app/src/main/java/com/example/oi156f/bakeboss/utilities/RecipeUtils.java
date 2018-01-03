package com.example.oi156f.bakeboss.utilities;

import android.app.Activity;

import com.example.oi156f.bakeboss.components.Ingredient;
import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.components.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by oi156f on 12/7/2017.
 *
 * Utility functions
 */

public final class RecipeUtils {

    public static String loadJSONFromAsset(Activity activity) {
        String json = null;
        try {
            InputStream is = activity.getAssets().open("bakingrecipes.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Recipe[] getRecipesFromJson(String json) throws JSONException {

        final String ID = "id";
        final String NAME = "name";
        final String INGREDIENTS = "ingredients";
        final String STEPS = "steps";
        final String SERVINGS = "servings";
        final String IMAGE = "image";

        JSONArray jRecipesArray = new JSONArray(json);

        Recipe[] recipes = new Recipe[jRecipesArray.length()];

        for (int i = 0; i < jRecipesArray.length(); i++) {
            JSONObject jRecipe = jRecipesArray.getJSONObject(i);
            Recipe recipe = new Recipe();
            recipe.setId(jRecipe.getInt(ID));
            recipe.setName(jRecipe.getString(NAME));
            recipe.setIngredients(getIngredientsFromJson(jRecipe.getJSONArray(INGREDIENTS)));
            recipe.setSteps(getStepsFromJson(jRecipe.getJSONArray(STEPS)));
            recipe.setServings(jRecipe.getInt(SERVINGS));
            recipe.setImage(jRecipe.getString(IMAGE));
            recipes[i] = recipe;
        }

        return recipes;
    }

    private static Ingredient[] getIngredientsFromJson(JSONArray jIngredientsArray) throws JSONException {

        final String NAME = "ingredient";
        final String QUANTITY = "quantity";
        final String MEASURE = "measure";

        Ingredient[] ingredients = new Ingredient[jIngredientsArray.length()];

        for (int i = 0; i < jIngredientsArray.length(); i++) {
            JSONObject jIngredient = jIngredientsArray.getJSONObject(i);
            Ingredient ingredient = new Ingredient();
            ingredient.setName(jIngredient.getString(NAME));
            ingredient.setQuantity(jIngredient.getDouble(QUANTITY));
            ingredient.setMeasure(jIngredient.getString(MEASURE).toLowerCase());
            ingredients[i] = ingredient;
        }

        return ingredients;
    }

    private static Step[] getStepsFromJson(JSONArray jStepsArray) throws JSONException  {

        final String ID = "id";
        final String TITLE = "shortDescription";
        final String DESCRIPTION = "description";
        final String VIDEO = "videoURL";
        final String THUMBNAIL = "thumbnailURL";

        Step[] steps = new Step[jStepsArray.length()];

        for (int i = 0; i < jStepsArray.length(); i++) {
            JSONObject jStep = jStepsArray.getJSONObject(i);
            Step step = new Step();
            step.setId(jStep.getInt(ID));
            step.setTitle(jStep.getString(TITLE));
            step.setDescription(jStep.getString(DESCRIPTION));
            step.setVideoUrl(jStep.getString(VIDEO));
            step.setThumbnailUrl(jStep.getString(THUMBNAIL));
            steps[i] = step;
        }

        return steps;
    }
}
