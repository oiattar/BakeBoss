package com.example.oi156f.bakeboss.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.oi156f.bakeboss.R;

import java.util.ArrayList;

/**
 * Created by oi156f on 1/17/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {

    private ArrayList<String> mIngredients;
    private Context mContext;

    public IngredientsAdapter(Context context, ArrayList<String> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View recipeView = inflater.inflate(R.layout.ingredients_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String ingredient = mIngredients.get(position);
        holder.ingredientName.setText(ingredient);
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredientName;

        public ViewHolder(View itemView) {
            super(itemView);
            ingredientName = itemView.findViewById(R.id.ingredient_name);
        }
    }
}
