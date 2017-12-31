package com.example.oi156f.bakeboss;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.utilities.RecipeUtils;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeListFragment extends Fragment {

    @BindView(R.id.recipe_list) RecyclerView rvRecipes;

    private Unbinder unbinder;

    public RecipeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        String recipeJson = RecipeUtils.loadJSONFromAsset(getActivity());
        Recipe[] recipes = null;
        try {
            recipes = RecipeUtils.getRecipesFromJson(recipeJson);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RecipeAdapter adapter = new RecipeAdapter(getActivity(), recipes);
        rvRecipes.setAdapter(adapter);
        rvRecipes.setLayoutManager(new LinearLayoutManager(getActivity()));
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
