package com.example.oi156f.bakeboss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oi156f.bakeboss.components.Recipe;
import com.example.oi156f.bakeboss.RecipeDetailFragment.*;

import java.util.ArrayList;

/**
 * Created by oi156f on 1/17/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<String> mSteps;
    private Recipe mRecipe;

    public OnStepClickListener mCallback;

    public StepsAdapter(Context context, ArrayList<String> steps, Recipe recipe, OnStepClickListener callback) {
        mContext = context;
        mSteps = steps;
        mRecipe = recipe;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View recipeView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String step = mSteps.get(position);
        holder.stepName.setText(step);
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView stepName;

        public ViewHolder(View itemView) {
            super(itemView);
            stepName = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mCallback.onStepSelected(mRecipe, getAdapterPosition());
            /*int position = getAdapterPosition();
            if (mContext.getResources().getBoolean(R.bool.isTablet)) {
                if (curPosition != position) {
                    FragmentManager fragmentManager = mContext.getSupportFragmentManager();
                    curPosition = position;
                    StepDetailFragment stepFragment = new StepDetailFragment();
                    stepFragment.setRecipe(mRecipe);
                    stepFragment.setPosition(position);
                    stepFragment.setTwoPane(true);
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_detail_container, stepFragment)
                            .commit();
                }
            } else {
                Intent intent = new Intent(mContext, StepDetailActivity.class);
                intent.putExtra(mContext.getString(R.string.selected_recipe_intent_tag), mRecipe);
                intent.putExtra(mContext.getString(R.string.selected_step_intent_tag), position);
                mContext.startActivity(intent);
            }*/
        }
    }
}
