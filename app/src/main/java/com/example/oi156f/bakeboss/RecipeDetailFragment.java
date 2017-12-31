package com.example.oi156f.bakeboss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.oi156f.bakeboss.components.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.ingredients_list)
    ListView ingredientsList;

    private Unbinder unbinder;

    private Recipe recipe = null;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if(intent.hasExtra("SelectedRecipe")) {
            recipe = intent.getParcelableExtra("SelectedRecipe");
            getActivity().setTitle(recipe.getName());
            ArrayAdapter<String> ingredientsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, recipe.getIngredientsList());
            ingredientsList.setAdapter(ingredientsAdapter);
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
