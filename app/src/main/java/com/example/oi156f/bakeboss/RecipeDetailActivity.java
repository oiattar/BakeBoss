package com.example.oi156f.bakeboss;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.oi156f.bakeboss.components.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private boolean mTwoPane;
    private Recipe recipe;
    private int curPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        if(findViewById(R.id.recipe_linear_layout) != null) {
            mTwoPane = true;

            if(intent.hasExtra(getString(R.string.selected_recipe_intent_tag))) {
                recipe = intent.getParcelableExtra(getString(R.string.selected_recipe_intent_tag));
            }

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                StepDetailFragment stepFragment = new StepDetailFragment();
                curPosition = 0;
                stepFragment.setRecipe(recipe);
                stepFragment.setPosition(curPosition);
                stepFragment.setTwoPane(true);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, stepFragment)
                        .commit();
            }
        } else
            mTwoPane = false;
    }

    @Override
    public void onStepSelected(Recipe recipe, int position) {

        if (mTwoPane) {
            if (curPosition != position) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                curPosition = position;
                StepDetailFragment stepFragment = new StepDetailFragment();
                stepFragment.setRecipe(recipe);
                stepFragment.setPosition(position);
                stepFragment.setTwoPane(true);
                fragmentManager.beginTransaction()
                        .replace(R.id.step_detail_container, stepFragment)
                        .commit();
            }
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra(getString(R.string.selected_recipe_intent_tag), recipe);
            intent.putExtra(getString(R.string.selected_step_intent_tag), position);
            startActivity(intent);
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }

}
