package com.cs3270final.mycookbook.ui.search;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.RecipeFull;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    private List<RecipeFull> recipes;

    public void setRecipes(List<RecipeFull> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public interface ViewRecipeFragmentLoader {
        void viewRecipe(Long recipeId);
    }

    // Allows switching to the ViewRecipe fragment
    private ViewRecipeFragmentLoader viewRecipeFragmentLoader;

    public RecipeAdapter(Context context) {
        recipes = new ArrayList<RecipeFull>();
        viewRecipeFragmentLoader = (ViewRecipeFragmentLoader) context;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecipeAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        holder.bindItem(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ShapeableImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.recipe_item_title);
            image = itemView.findViewById(R.id.recipe_item_image);
        }

        public void bindItem(RecipeFull recipeFull) {
            title.setText(recipeFull.recipe.getName());

            if (recipeFull.recipe.getImage_path() != null) {
                Glide.with(image)
                    .load(Uri.parse("file://" + recipeFull.recipe.getImage_path()))
                    .into(image);
            }

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    viewRecipeFragmentLoader.viewRecipe(recipeFull.recipe.getId());
                }

            });
        }

    }
}
