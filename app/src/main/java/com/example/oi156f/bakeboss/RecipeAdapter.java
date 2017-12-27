package com.example.oi156f.bakeboss;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oi156f.bakeboss.components.Recipe;
import com.squareup.picasso.Picasso;

/**
 * Created by oi156f on 12/8/2017.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private Recipe[] mRecipes;
    private Context mContext;

    public RecipeAdapter(Context context, Recipe[] recipes) {
        mContext = context;
        mRecipes = recipes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View recipeView = inflater.inflate(R.layout.recipe_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recipeView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipes[position];
        holder.recipeTitle.setText(recipe.getName());
        Picasso.with(mContext)
                .load(recipe.getImage())
                .error(R.drawable.image_error)
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return mRecipes.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recipeTitle;
        public ImageView recipeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            recipeTitle = itemView.findViewById(R.id.recipe_title);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Recipe recipe = mRecipes[getAdapterPosition()];
            Intent intent = new Intent(mContext, RecipeDetailActivity.class);
            intent.putExtra("SelectedRecipe", recipe);
            mContext.startActivity(intent);
        }
    }
}
