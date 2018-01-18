package com.example.oi156f.bakeboss.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oi156f.bakeboss.FetchRecipesTask;
import com.example.oi156f.bakeboss.R;
import com.example.oi156f.bakeboss.components.Recipe;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * The configuration screen for the {@link RecipeWidgetProvider RecipeWidgetProvider} AppWidget.
 */
public class RecipeWidgetProviderConfigureActivity extends Activity implements FetchRecipesTask.OnTaskCompleted {

    private static final String PREFS_NAME = "com.example.oi156f.bakeboss.widget.RecipeWidgetProvider";
    private static final String POSITION_KEY = "position_";
    private static final String NAME_KEY = "recipe_";
    private static final String INGREDIENTS_KEY = "ing_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    ListView recipeList;

    public RecipeWidgetProviderConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);
        recipeList = findViewById(R.id.recipe_select_list);

        new FetchRecipesTask(this).execute();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    @Override
    public void onTaskCompleted(final Recipe[] recipes) {
        ArrayList<String> recipeNames = new ArrayList<>();
        for (Recipe recipe : recipes) {
            recipeNames.add(recipe.getName());
        }
        ArrayAdapter<String> recipesAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipeNames);
        recipeList.setAdapter(recipesAdapter);
        recipeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Context context = RecipeWidgetProviderConfigureActivity.this;

                String recipeName = recipes[i].getName();
                ArrayList<String> ingredientsList = recipes[i].getIngredientsList();
                saveIngredientsPref(context, mAppWidgetId, i, recipeName, ingredientsList);

                // It is the responsibility of the configuration activity to update the app widget
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                RecipeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                // Make sure we pass back the original appWidgetId
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            }
        });
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveIngredientsPref(Context context, int appWidgetId, int recipePosition, String recipeName, ArrayList<String> ingredients) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(POSITION_KEY + appWidgetId, recipePosition);
        Gson gson = new Gson();
        String text = gson.toJson(ingredients);
        prefs.putString(NAME_KEY + appWidgetId, recipeName);
        prefs.putString(INGREDIENTS_KEY + appWidgetId, text);
        prefs.apply();
    }

    static String loadIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String ingJson = prefs.getString(INGREDIENTS_KEY + appWidgetId, null);
        return ingJson;
    }

    static String loadNamePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String recipeName = prefs.getString(NAME_KEY + appWidgetId, null);
        return recipeName;
    }

    static int loadPosPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int position = prefs.getInt(POSITION_KEY + appWidgetId, 0);
        return position;
    }

    static void deleteIngredientsPref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(NAME_KEY + appWidgetId);
        prefs.remove(INGREDIENTS_KEY + appWidgetId);
        prefs.apply();
    }
}

