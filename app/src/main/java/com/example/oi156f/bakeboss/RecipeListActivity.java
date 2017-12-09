package com.example.oi156f.bakeboss;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;

import org.json.JSONException;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        RecyclerView rvRecipes = findViewById(R.id.recipe_list);
        String recipeJson = RecipeUtils.loadJSONFromAsset(this);
        Recipe[] recipes = null;
        try {
            recipes = RecipeUtils.getRecipesFromJson(recipeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecipeAdapter adapter = new RecipeAdapter(this, recipes);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
    }
}
