package com.example.oi156f.bakeboss;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oi156f.bakeboss.components.Recipe;

public class RecipeDetailFragment extends Fragment {

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
        TextView recipeName = rootView.findViewById(R.id.recipe_name);
        Intent intent = getActivity().getIntent();
        if(intent.hasExtra("SelectedRecipe")) {
            recipe = intent.getParcelableExtra("SelectedRecipe");
            recipeName.setText(recipe.getName());
        }
        return rootView;
    }
}
