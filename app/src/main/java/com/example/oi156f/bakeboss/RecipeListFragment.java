package com.example.oi156f.bakeboss;

import android.os.Bundle;
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

public class RecipeListFragment extends Fragment {

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
        new FetchRecipesTask(getActivity(), rootView).execute();
        return rootView;
    }
}
