package com.example.oi156f.bakeboss.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.oi156f.bakeboss.R;
import com.google.gson.Gson;

/**
 * Created by oi156f on 1/15/2018.
 */

public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {

    private String[] ingList;
    private Context context;
    private int appWidgetId;

    public WidgetListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart());

        String ingJson = RecipeWidgetProviderConfigureActivity.loadIngredientsPref(context, appWidgetId);
        Gson gson = new Gson();
        ingList = gson.fromJson(ingJson, String[].class);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingList.length;
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.ingredients_list_item);
        remoteView.setTextViewText(R.id.ingredient_name, ingList[i]);
        //remoteView.setTextViewTextSize(R.id.ingredient_name, TypedValue.COMPLEX_UNIT_SP, 14);
        remoteView.setViewPadding(R.id.ingredient_name, 5, 5, 5, 5);
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
