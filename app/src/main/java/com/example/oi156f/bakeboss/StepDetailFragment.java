package com.example.oi156f.bakeboss;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.components.Step;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class StepDetailFragment extends Fragment {

    @BindView(R.id.step_instruction)
    TextView stepInstruction;

    private Unbinder unbinder;

    private Recipe recipe = null;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        Intent intent = getActivity().getIntent();
        if(intent.hasExtra(getString(R.string.selected_recipe_intent_tag))) {
            recipe = intent.getParcelableExtra(getString(R.string.selected_recipe_intent_tag));
            getActivity().setTitle(recipe.getName());
            int position = intent.getIntExtra(getString(R.string.selected_step_intent_tag), 0);
            Step[] steps = recipe.getSteps();
            Step selectedStep = steps[position];
            stepInstruction.setText(selectedStep.getDescription());
        }
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
