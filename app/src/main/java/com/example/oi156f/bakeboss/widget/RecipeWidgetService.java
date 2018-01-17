package com.example.oi156f.bakeboss.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by oi156f on 1/15/2018.
 */

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        int appWidgetId = Integer.valueOf(intent.getData().getSchemeSpecificPart());

        return new WidgetListProvider(this.getApplicationContext(), intent);
    }
}
