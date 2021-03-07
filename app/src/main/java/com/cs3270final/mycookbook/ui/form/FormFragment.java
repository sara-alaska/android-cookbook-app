package com.cs3270final.mycookbook.ui.form;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.cs3270final.mycookbook.MainActivity;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.Ingredient;
import com.cs3270final.mycookbook.db.Instruction;
import com.cs3270final.mycookbook.db.Recipe;
import com.cs3270final.mycookbook.db.RecipeFull;
import com.cs3270final.mycookbook.db.RecipeTag;
import com.cs3270final.mycookbook.db.Tag;
import com.cs3270final.mycookbook.ui.search.RecipeViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FormFragment extends Fragment implements IngredientAdapter.IngredientListener, InstructionAdapter.InstructionListener {

    private View root;
    private RecyclerView ingredientRecyclerView;
    private RecyclerView instructionRecyclerView;
    private IngredientAdapter ingredientAdapter;
    private InstructionAdapter instructionAdapter;
    private TextInputEditText viewTitleInput;
    private ImageView viewRecipeImage;
    private ChipGroup viewTagsContainer;
    private ImageView viewTagCreateButton;
    private TextInputEditText viewTagInput;
    private List<Tag> tags = new ArrayList<>();
    private Button saveButton;
    private RecipeFull recipeFull;
    private String selectedImagePath;
    private String[] unitOptions;

    // Navigate to the Search fragment upon saving the recipe and hide keyboard
    public interface FormAndKeyboardHandler {
        void navigateToSearch();
        void hideKeyboard(View view);
        void setToolbarTitle(String title);
    }

    private FormAndKeyboardHandler formAndKeyboardHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_form, container, false);
        initViews();

        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        formAndKeyboardHandler = (FormAndKeyboardHandler) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        initIngredientRecyclerView();
        initInstructionRecyclerView();
        initRecipeFullIfEditing();
        initRecipe();
        initIngredients();
        initInstructions();
        initUnitOptions();
        initTags();
    }

    // Gets called after choosing the image from the phone library.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            Image image = ImagePicker.getFirstImageOrNull(data);
            selectedImagePath = image.getPath();
            Glide.with(viewRecipeImage)
                .load(image.getUri())
                .into(viewRecipeImage);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        viewTitleInput = root.findViewById(R.id.form_title_input);
        viewRecipeImage = root.findViewById(R.id.form_recipe_image);
        viewTagsContainer = root.findViewById(R.id.form_tags_chip_group);
        viewTagInput = root.findViewById(R.id.tag_input);
        viewTagCreateButton = root.findViewById(R.id.tag_enter_button);
        saveButton = root.findViewById(R.id.save_button);

        Fragment formFragment = this;
        viewRecipeImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Starts the Image Picker
                ImagePicker.create(formFragment)
                    .toolbarImageTitle(getString(R.string.image_pick_recipe_title))
                    .theme(R.style.Theme_MyCookbook)
                    .single()
                    .start();

            }

        });

        viewTagCreateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addNewTag();
            }

        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveRecipe();
            }

        });

    }

    // Set up RecipeFull if it is not null
    private void initRecipe() {
        if (recipeFull != null) {

            viewTitleInput.setText(recipeFull.recipe.getName());
            selectedImagePath = recipeFull.recipe.getImage_path();
            // Loads the image into the Recipe imageview
            Glide.with(viewRecipeImage)
                    .load(Uri.parse("file://" + recipeFull.recipe.getImage_path()))
                    .into(viewRecipeImage);
        }
    }

    private void initIngredients() {
        if (recipeFull != null) {

            List<IngredientInputView> ingredients = new ArrayList<>();

            // Loops through the Ingredient list and creates corresponding views
            for (Ingredient ingredient : recipeFull.ingredientList) {

                IngredientInputView converted = new IngredientInputView();
                converted.setId(ingredient.getId());
                converted.setName(ingredient.getName());
                converted.setAmount(ingredient.getAmount());
                converted.setUnit(ingredient.getUnit());
                converted.showAddButton = false;
                buildUnitDialog(converted);
                ingredients.add(converted);

            }

            ingredients.get(ingredients.size() - 1).showAddButton = true;
            ingredientAdapter.setIngredients(ingredients);

        } else {

            List<IngredientInputView> ingredients = ingredientAdapter.getIngredients();

            if (ingredients.isEmpty()) {
                addNewIngredient();
            }

        }
    }

    private void initInstructions() {
        if (recipeFull != null) {

            List<InstructionInputView> instructions = new ArrayList<>();
            for (Instruction instruction : recipeFull.instructionList) {
                InstructionInputView converted = new InstructionInputView();
                converted.setId(instruction.getId());
                converted.setStepNumber(instruction.getStepNumber());
                converted.setStepName(instruction.getStepName());
                converted.showAddButton = false;
                instructions.add(converted);
            }

            instructions.get(instructions.size() - 1).showAddButton = true;
            instructionAdapter.setInstructions(instructions);

        } else {

            List<InstructionInputView> instructions = instructionAdapter.getInstructions();

            if (instructions.isEmpty()) {
                addNewInstruction();
            }

        }
    }

    // Populates the RecipeFull with Recipe data for edit
    private void initRecipeFullIfEditing() {

        Bundle args = getArguments();
        if (args != null) {

            Long recipeId = args.getLong(MainActivity.BUNDLE_KEY_RECIPE_ID);
            if (recipeId > 0) {
                formAndKeyboardHandler.setToolbarTitle(getString(R.string.edit_recipe));
                List<RecipeFull> recipes = new ViewModelProvider(requireActivity())
                    .get(RecipeViewModel.class).recipes.getValue();
                // Find the recipe that matches the Bundle argument from our LiveData
                recipeFull = recipes.stream().filter(rf -> rf.recipe.getId().equals(recipeId)).findFirst().orElse(null);
            }
        }
    }

    private void initIngredientRecyclerView() {
        if (ingredientRecyclerView == null) {
            ingredientRecyclerView = root.findViewById(R.id.ingredient_recycler_view);
            ingredientAdapter = new IngredientAdapter(this);
            ingredientRecyclerView.setAdapter(ingredientAdapter);
            ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

    private void initInstructionRecyclerView() {
        if (instructionRecyclerView == null) {
            instructionRecyclerView = root.findViewById(R.id.instruction_recycler_view);
            instructionAdapter = new InstructionAdapter(this);
            instructionRecyclerView.setAdapter(instructionAdapter);
            instructionRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }

    // Gets units array for the unit selection dialog
    public void initUnitOptions() {
        if (unitOptions == null) {
            unitOptions = getResources().getStringArray(R.array.units);
        }
    }

    // Initializing tags and building a view for them
    public void initTags() {
        if (recipeFull != null) {

            tags = recipeFull.tagList;
            for (Tag tag : tags) {

                Chip chip = new Chip(getContext());
                chip.setText(tag.getName());
                chip.setOnCloseIconClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        viewTagsContainer.removeView(v);
                        tags.remove(tag);
                        deleteTag(tag);
                    }

                });

                viewTagsContainer.addView(chip);

            }

        } else {
            tags = new ArrayList<>();
        }
    }

    // Building a unit selection dialog for every ingredient
    public void buildUnitDialog(IngredientInputView ingredient) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        final int[] selectedItem = {0};

        builder.setTitle(R.string.select_units)
            .setSingleChoiceItems(R.array.units, 0, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedItem[0] = i;
                }

            }).setPositiveButton(getResources().getString(R.string.dialog_positive_button_text), new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ingredient.setUnit(unitOptions[selectedItem[0]]);
                    ingredientAdapter.updateIngredient(ingredient);
                }

            }).setNegativeButton(getResources().getString(R.string.dialog_negative_button_text), null);

        ingredient.unitDialog = builder.create();
    }

    // Adds a blank ingredient and scrolls to it.
    @Override
    public void addNewIngredient() {
        IngredientInputView ingredient = new IngredientInputView();
        ingredient.setUnit(getResources().getString(R.string.form_ingredient_option_unit_cups));
        buildUnitDialog(ingredient);
        ingredientAdapter.addIngredient(ingredient);
        ingredientRecyclerView.smoothScrollToPosition(ingredientAdapter.getItemCount());
    }

    @Override
    public void addNewInstruction() {
        instructionAdapter.addInstruction(new InstructionInputView());
        instructionRecyclerView.smoothScrollToPosition(instructionAdapter.getItemCount());
    }

    // Builds the recipe portion of the RecipeFull
    public void buildRecipe() {
        if (recipeFull == null) {
            recipeFull = new RecipeFull();
            recipeFull.recipe = new Recipe();
        }

        recipeFull.recipe.setName(viewTitleInput.getText().toString());
        recipeFull.recipe.setImage_path(selectedImagePath);
        recipeFull.recipe.setDate_created(new Date());
    }

    public void addNewTag() {
        String tagName = viewTagInput.getText().toString();
        if (!tagName.isEmpty()) {

            Tag tag = new Tag(tagName);
            tags.add(tag);

            Chip chip = new Chip(getContext());
            chip.setText(tag.getName());
            chip.setCheckable(false);
            chip.setOnCloseIconClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    viewTagsContainer.removeView(v);
                    tags.remove(tag);
                }

            });

            viewTagsContainer.addView(chip);
            viewTagInput.setText(null);

        }
    }

    public void deleteTag(Tag tag) {
        if (recipeFull != null && recipeFull.recipe != null && recipeFull.recipe.getId() != null) {

            new Thread(new Runnable() {

                @Override
                public void run() {
                    AppDatabase db = AppDatabase.getInstance(getContext());
                    db.tagRecipeDAO().delete(new RecipeTag(recipeFull.recipe.getId(), tag.getId()));
                }

            }).start();

        }
    }

    public void saveRecipe() {
        buildRecipe();

        new Thread(new Runnable() {

            @Override
            public void run() {

                AppDatabase db = AppDatabase.getInstance(getContext());
                if (recipeFull.recipe.getId() == null) {
                    recipeFull.recipe.setId(db.recipeDAO().insert(recipeFull.recipe));
                } else {
                    db.recipeDAO().update(recipeFull.recipe);
                }

                List<IngredientInputView> ingredients = ingredientAdapter.getIngredients();
                for (IngredientInputView ingredient : ingredients) {
                    ingredient.setRecipe_id(recipeFull.recipe.getId());
                    if (ingredient.getId() == null) {
                        db.ingredientDAO().insert(ingredient);
                    } else {
                        db.ingredientDAO().update(ingredient);
                    }
                }

                List<InstructionInputView> instructions = instructionAdapter.getInstructions();
                for (int i = 0; i < instructions.size(); i++) {
                    InstructionInputView instruction = instructions.get(i);
                    instruction.setRecipe_id(recipeFull.recipe.getId());
                    instruction.setStepNumber(i + 1);
                    if (instruction.getId() == null) {
                        db.instructionDAO().insert(instruction);
                    } else {
                        db.instructionDAO().update(instruction);
                    }
                }

                for (Tag tag : tags) {
                    Long tagId = tag.getId();
                    if (tagId == null) {
                        tagId = db.tagDAO().insert(tag);
                        db.tagRecipeDAO().insert(new RecipeTag(recipeFull.recipe.getId(), tagId));
                    }
                }
            }

        }).start();

        formAndKeyboardHandler.hideKeyboard(root);
        formAndKeyboardHandler.navigateToSearch();

    }
}