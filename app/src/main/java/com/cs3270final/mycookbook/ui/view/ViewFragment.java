package com.cs3270final.mycookbook.ui.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.cs3270final.mycookbook.MainActivity;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.Ingredient;
import com.cs3270final.mycookbook.db.RecipeFull;
import com.cs3270final.mycookbook.db.ShoppingListItem;
import com.cs3270final.mycookbook.db.Tag;
import com.cs3270final.mycookbook.ui.search.RecipeViewModel;

import java.util.List;

public class ViewFragment extends Fragment implements IngredientAdapter.CartButtonHandler {

    private View root;
    private RecipeFull recipeFull;
    private IngredientAdapter ingredientAdapter;
    private InstructionAdapter instructionAdapter;
    private MaterialTextView viewRecipeTitle;
    private ImageView viewRecipeEditButton;
    private ShapeableImageView viewRecipeImage;
    private ChipGroup viewTags;

    public interface FragmentLoader {
        public void navigateToEditRecipeForm(Long recipeId);
    }

    private FragmentLoader fragmentLoader;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_view, container, false);

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        fragmentLoader = (FragmentLoader) context;

    }

    @Override
    public void onResume() {
        super.onResume();
        initIngredientRecyclerView();
        initInstructionRecyclerView();
        initRecipe(getArguments().getLong(MainActivity.BUNDLE_KEY_RECIPE_ID));
        initViews();
        displayRecipe();
    }

    private void initRecipe(Long recipeId) {
        List<RecipeFull> recipes = new ViewModelProvider(requireActivity()).get(RecipeViewModel.class).recipes.getValue();
        recipeFull = recipes.stream().filter(recipeFull -> recipeFull.recipe.getId().equals(recipeId)).findFirst().orElse(null);

    }

    private void initIngredientRecyclerView() {
        RecyclerView ingredientRecyclerView = root.findViewById(R.id.view_ingredients_recyclerView);
        ingredientAdapter = new IngredientAdapter(this);
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void initInstructionRecyclerView() {
        RecyclerView instructionRecyclerView = root.findViewById(R.id.view_instructions_recycler_view);
        instructionAdapter = new InstructionAdapter();
        instructionRecyclerView.setAdapter(instructionAdapter);
        instructionRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), LinearLayoutManager.VERTICAL, false));

    }

    private void initViews() {
        viewRecipeTitle = root.findViewById(R.id.view_recipe_title);
        viewRecipeImage = root.findViewById(R.id.form_recipe_image);
        viewTags = root.findViewById(R.id.view_tags);
        viewRecipeEditButton = root.findViewById(R.id.recipe_edit_button);
        viewRecipeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentLoader.navigateToEditRecipeForm(recipeFull.recipe.getId());
            }
        });

    }

    private void displayRecipe() {
        viewRecipeTitle.setText(recipeFull.recipe.getName());

        String imagePath = recipeFull.recipe.getImage_path();
        if (imagePath != null && !imagePath.isEmpty()) {
            Glide.with(viewRecipeImage)
                    .load(Uri.parse("file://" + recipeFull.recipe.getImage_path()))
                    .into(viewRecipeImage);
        }

        ingredientAdapter.setIngredients(recipeFull.ingredientList);
        instructionAdapter.setInstructionItems(recipeFull.instructionList);

        for (Tag tag : recipeFull.tagList) {
            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setCloseIconVisible(false);
            viewTags.addView(chip);
        }
    }

    @Override
    public void addToShoppingList(Ingredient ingredient) {
        new Thread(new Runnable() {

            @Override
            public void run() {

                AppDatabase db = AppDatabase.getInstance(getContext());
                ShoppingListItem shoppingListItem = new ShoppingListItem(ingredient.getId(), false);
                db.shoppingListItemDAO().insert(shoppingListItem);

            }

        }).start();

        Toast.makeText(getContext(), ingredient.getName() + " was added to your shopping list.", Toast.LENGTH_SHORT).show();
    }

}
