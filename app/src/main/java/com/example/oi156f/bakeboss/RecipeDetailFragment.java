package com.example.oi156f.bakeboss;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.oi156f.bakeboss.components.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.ingredients_list)
    ListView ingredientsList;
    @BindView(R.id.steps_list)
    ListView stepsList;

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
        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(getString(R.string.selected_recipe_intent_tag));
        }
        if(intent.hasExtra(getString(R.string.selected_recipe_intent_tag))) {
            recipe = intent.getParcelableExtra(getString(R.string.selected_recipe_intent_tag));
        }
        if (recipe != null) {
            getActivity().setTitle(recipe.getName());
            ArrayAdapter<String> ingredientsAdapter =
                    new ArrayAdapter<>(getContext(), R.layout.ingredients_list_item, recipe.getIngredientsList());
            ingredientsList.setAdapter(ingredientsAdapter);
            setListViewHeightBasedOnChildren(ingredientsList);
            ArrayAdapter<String> stepsAdapter =
                    new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recipe.getStepsList());
            stepsList.setAdapter(stepsAdapter);
            setListViewHeightBasedOnChildren(stepsList);
            stepsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getContext(), StepDetailActivity.class);
                    intent.putExtra(getString(R.string.selected_recipe_intent_tag), recipe);
                    intent.putExtra(getString(R.string.selected_step_intent_tag), i);
                    getContext().startActivity(intent);
                }
            });
        }
        return rootView;
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

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
