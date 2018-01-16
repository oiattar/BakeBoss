package com.example.oi156f.bakeboss.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;

import com.example.oi156f.bakeboss.R;
import com.example.oi156f.bakeboss.RecipeDetailActivity;
import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;
import com.google.gson.Gson;

import org.json.JSONException;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetProviderConfigureActivity RecipeWidgetProviderConfigureActivity}
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        String recipeName = RecipeWidgetProviderConfigureActivity.loadNamePref(context, appWidgetId);
        views.setTextViewText(R.id.widget_recipe_title, recipeName);

        Intent listIntent = new Intent(context, RecipeWidgetService.class);
        listIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId), null));
        views.setRemoteAdapter(R.id.widget_ingredients_list, listIntent);

        int position = RecipeWidgetProviderConfigureActivity.loadPosPref(context, appWidgetId);
        Log.d("OMAR", "recipePosition: " + Integer.toString(position));
        String recipeJson = RecipeUtils.loadJSONFromAsset(context);
        Recipe[] recipes = null;
        try {
            recipes = RecipeUtils.getRecipesFromJson(recipeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Recipe recipe = recipes[position];
        Log.d("OMAR", "recipeName: " + recipe.getName());
        Intent launchIntent = new Intent(context, RecipeDetailActivity.class);
        launchIntent.putExtra(context.getString(R.string.selected_recipe_intent_tag), recipe);
        Recipe rec = launchIntent.getParcelableExtra(context.getString(R.string.selected_recipe_intent_tag));
        Log.d("OMAR", "recName: " + rec.getName());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetProviderConfigureActivity.deleteIngredientsPref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

