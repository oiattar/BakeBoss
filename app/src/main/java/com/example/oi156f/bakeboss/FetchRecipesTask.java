package com.example.oi156f.bakeboss;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.oi156f.bakeboss.adapters.RecipeAdapter;
import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by oi156f on 1/17/2018.
 */

public class FetchRecipesTask extends AsyncTask<Void, Void, Recipe[]> {

    private final Context mContext;
    @BindView(R.id.recipe_list)
    RecyclerView recipeList;

    private final Unbinder unbinder;

    public FetchRecipesTask(Context context, View view) {
        mContext = context;
        unbinder = ButterKnife.bind(this, view);
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
        boolean isTablet = mContext.getResources().getBoolean(R.bool.isTablet);
        int numColumns;
        if (isTablet) { //it's a tablet
            numColumns = 3;
        } else { //it's a phone, not a tablet
            numColumns = 1;
        }
        for (int i = 0; i < recipes.length; i++) {
            recipes[i].setImage(RecipeUtils.imageURLS[i]);
        }
        RecipeAdapter adapter = new RecipeAdapter(mContext, recipes);
        recipeList.setAdapter(adapter);
        recipeList.setLayoutManager(new GridLayoutManager(mContext, numColumns));
        unbinder.unbind();
    }
}
