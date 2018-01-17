package com.example.oi156f.bakeboss.utilities;

import android.content.Context;
import android.net.Uri;

import com.example.oi156f.bakeboss.components.Ingredient;
import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.components.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by oi156f on 12/7/2017.
 *
 * Utility functions
 */

public final class RecipeUtils {

    public static final String RECIPE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static final String NO_VIDEO_URL = "https://i.imgur.com/pi0OsD7.jpg";

    public static String[] imageURLS = {
                        "https://i.imgur.com/fX9XwDN.png",
                        "http://food.fnr.sndimg.com/content/dam/images/food/fullset/2016/2/18/1/FNK_Brownie-Guide-Classic-Brownies_s4x3.jpg.rend.hgtvcom.616.462.suffix/1456176242492.jpeg",
                        "http://d3cizcpymoenau.cloudfront.net/images/28462/SFS_yellow_layer_cake-195.jpg",
                        "https://i.imgur.com/D3BSS8q.jpg"};

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("bakingrecipes.json");
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

    /**
     * Builds the URL used to talk to the movie database
     *
     * @return The URL to use to query the recipes
     */
    public static URL buildUrl() {

        Uri builtUri = Uri.parse(RECIPE_URL).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
