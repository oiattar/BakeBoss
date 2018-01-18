package com.example.oi156f.bakeboss;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oi156f.bakeboss.adapters.RecipeAdapter;
import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment implements FetchRecipesTask.OnTaskCompleted {

    @BindView(R.id.recipe_list)
    RecyclerView recipeList;

    private Unbinder unbinder;

    private Recipe[] mRecipes;

    private static final String RECIPE_TAG = "recipes";

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        if (savedInstanceState == null) {
            new FetchRecipesTask(this).execute();
        } else {
            mRecipes = (Recipe[]) savedInstanceState.getParcelableArray(RECIPE_TAG);
            setupRecipeList();
        }
        return rootView;
    }

    @Override
    public void onTaskCompleted(Recipe[] recipes) {
        mRecipes = recipes;
        setupRecipeList();
    }

    public void setupRecipeList() {
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int numColumns;
        if (isTablet) { //it's a tablet
            numColumns = 3;
        } else { //it's a phone, not a tablet
            numColumns = 1;
        }
        RecipeAdapter adapter = new RecipeAdapter(getActivity(), mRecipes);
        recipeList.setAdapter(adapter);
        recipeList.setLayoutManager(new GridLayoutManager(getActivity(), numColumns));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(RECIPE_TAG, mRecipes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
