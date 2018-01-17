package com.example.oi156f.bakeboss;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oi156f.bakeboss.components.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.ingredients_list)
    RecyclerView ingredientsList;
    @BindView(R.id.steps_list)
    RecyclerView stepsList;

    private Unbinder unbinder;

    private Recipe recipe = null;

    OnStepClickListener mCallback;

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
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(getString(R.string.selected_recipe_intent_tag));
        }
        if(intent.hasExtra(getString(R.string.selected_recipe_intent_tag))) {
            recipe = intent.getParcelableExtra(getString(R.string.selected_recipe_intent_tag));
        }
        if (recipe != null) {
            getActivity().setTitle(recipe.getName());
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getActivity(), recipe.getIngredientsList());
            ingredientsList.setAdapter(ingredientsAdapter);
            ingredientsList.setLayoutManager(new LinearLayoutManager(getActivity()));
            StepsAdapter stepsAdapter = new StepsAdapter(getActivity(), recipe.getStepsList(), recipe, mCallback);
            stepsList.setAdapter(stepsAdapter);
            stepsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return rootView;
    }

    public interface OnStepClickListener {
        void onStepSelected(Recipe recipe, int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.selected_recipe_intent_tag), recipe);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
