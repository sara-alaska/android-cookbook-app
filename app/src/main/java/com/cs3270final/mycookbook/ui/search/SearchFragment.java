package com.cs3270final.mycookbook.ui.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.Recipe;
import com.cs3270final.mycookbook.db.RecipeFull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class SearchFragment extends Fragment {

    private View root;
    private RecipeAdapter recipeAdapter;
    private List<RecipeFull> allRecipes = new ArrayList<>();
    private TextInputLayout searchBarLayout;
    private ImageView searchCloseButton;
    private EditText searchInput;

    private String[] sortOptions;
    private String sortOptionNewestFirst;
    private String sortOptionOldestFirst;
    private String sortOptionNameDesc;
    private String sortOptionNameAsc;
    private AlertDialog sortDialog;

    private static final int SORT_ASC = 1;
    private static final int SORT_DESC = 2;
    private static final int SORT_BY_NAME = 3;
    private static final int SORT_BY_DATE = 4;
    private int sortDirection = SORT_ASC;
    private int sortBy = SORT_BY_NAME;
    private String searchTerm = "";

    public interface KeyboardManipulator {
        public void showKeyboard(View view);
        public void hideKeyboard(View view);
    }

    private KeyboardManipulator keyboardManipulator;

    public interface FragmentLoader {
        public void navigateToNewRecipeForm();
    }

    private FragmentLoader fragmentLoader;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_search, container, false);
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.recipes_menu, menu);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        keyboardManipulator = (KeyboardManipulator) context;
        fragmentLoader = (FragmentLoader) context;
    }

    @Override
    public void onResume() {
        super.onResume();

        initSearchBar();
        initFab();
        setSearchBarVisibility(View.GONE);
        initSortOptions();
        initSortDialog();
        initRecyclerView();
        initRecipeData();
    }


    public void initSearchBar() {
        searchBarLayout = root.findViewById(R.id.search_recipes);
        searchInput = root.findViewById(R.id.search_input);
        searchInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    searchTerm = searchInput.getText().toString().trim();

                    if (!searchTerm.isEmpty()) {
                        recipeAdapter.setRecipes(getRecipesWithAppliedSortingAndFiltering(allRecipes));
                    }
                    keyboardManipulator.hideKeyboard(searchInput);

                    return true;
                }

                return false;
            }
        });

        searchCloseButton = root.findViewById(R.id.close_search);
        searchCloseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                searchInput.setText("");
                searchTerm = "";
                setSearchBarVisibility(View.GONE);
                recipeAdapter.setRecipes(getRecipesWithAppliedSortingAndFiltering(allRecipes));
                keyboardManipulator.hideKeyboard(searchInput);
            }

        });
    }

    private void initFab() {
        root.findViewById(R.id.add_fab).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                fragmentLoader.navigateToNewRecipeForm();
            }

        });
    }

    public void setSearchBarVisibility(Integer viewVisibility) {
        searchBarLayout.setVisibility(viewVisibility);
        searchCloseButton.setVisibility(viewVisibility);
    }

    private void initSortOptions() {
        sortOptionNewestFirst = getResources().getString(R.string.sort_option_newest_first);
        sortOptionOldestFirst = getResources().getString(R.string.sort_option_oldest_first);
        sortOptionNameDesc = getResources().getString(R.string.sort_option_name_desc);
        sortOptionNameAsc = getResources().getString(R.string.sort_option_name_asc);
    }

    private void initSortDialog() {
        sortOptions = getResources().getStringArray(R.array.sort_options);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        final int[] selection = {-1};
        builder.setTitle(getResources().getString(R.string.search_sort_by_title))
                .setSingleChoiceItems(R.array.sort_options, 0, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selection[0] = i;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.dialog_positive_button_text), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (selection[0] > -1 && sortOptions.length >= (selection[0] + 1)) {
                            onSortingChanged(sortOptions[selection[0]]);
                        }

                    }

                })
                .setNegativeButton(getResources().getString(R.string.dialog_negative_button_text), null);

        sortDialog = builder.create();
    }

    private void initRecyclerView() {
        RecyclerView recipesRecyclerView = root.findViewById(R.id.recipes_recycler_view);
        recipeAdapter = new RecipeAdapter(getContext());
        recipesRecyclerView.setAdapter(recipeAdapter);
        recipesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void initRecipeData() {
        new ViewModelProvider(requireActivity()).get(RecipeViewModel.class).getAllSortedByDateDesc(getContext())
            .observe(this, new Observer<List<RecipeFull>>() {

                @Override
                public void onChanged(@Nullable List<RecipeFull> recipeList) {

                    if (recipeList != null) {
                        allRecipes = recipeList;
                        recipeAdapter.setRecipes(getRecipesWithAppliedSortingAndFiltering(recipeList));
                    }

                }
            });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_option_search:
                setSearchBarVisibility(View.VISIBLE);
                searchInput.requestFocus();
                keyboardManipulator.showKeyboard(searchInput);
                break;

            case R.id.menu_option_sort:
                sortDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onSortingChanged(String selectedSortText) {
        // A switch statement doesn't work here because
        // the sort option strings cannot be final.
        if (selectedSortText.equals(sortOptionNewestFirst)) {
            sortDirection = SORT_DESC;
            sortBy = SORT_BY_DATE;

        } else if (selectedSortText.equals(sortOptionOldestFirst)) {
            sortDirection = SORT_ASC;
            sortBy = SORT_BY_DATE;

        } else if (selectedSortText.equals(sortOptionNameDesc)) {
            sortDirection = SORT_DESC;
            sortBy = SORT_BY_NAME;

        } else if (selectedSortText.equals(sortOptionNameAsc)) {
            sortDirection = SORT_ASC;
            sortBy = SORT_BY_NAME;
        }

        recipeAdapter.setRecipes(getRecipesWithAppliedSortingAndFiltering(allRecipes));
    }

    // Sorts and filters recipes
    private List<RecipeFull> getRecipesWithAppliedSortingAndFiltering(List<RecipeFull> recipes) {
        List<RecipeFull> recipesToModify = new ArrayList<RecipeFull>(recipes);

        return getSortedRecipes(getFilteredRecipes(recipesToModify));
    }

    private List<RecipeFull> getSortedRecipes(List<RecipeFull> recipes) {
        recipes.sort(new Comparator<RecipeFull>() {

            @Override
            public int compare(RecipeFull o1, RecipeFull o2) {

                int compareResult = 0;

                if (sortDirection == SORT_ASC) {

                    if (sortBy == SORT_BY_DATE) {
                        compareResult = o1.recipe.getDate_created().compareTo(o2.recipe.getDate_created());
                    } else if (sortBy == SORT_BY_NAME) {
                        compareResult = o1.recipe.getName().compareTo(o2.recipe.getName());
                    }

                } else if (sortDirection == SORT_DESC) {

                    if (sortBy == SORT_BY_DATE) {
                        compareResult = o2.recipe.getDate_created().compareTo(o1.recipe.getDate_created());
                    } else if (sortBy == SORT_BY_NAME) {
                        compareResult = o2.recipe.getName().compareTo(o1.recipe.getName());
                    }
                }

                return compareResult;
            }
        });

        return recipes;
    }

    private List<RecipeFull> getFilteredRecipes(List<RecipeFull> recipes) {
        if (!searchTerm.isEmpty()) {
            List<RecipeFull> matchedRecipes = new ArrayList<RecipeFull>();

            for (RecipeFull recipeFull : recipes) {

                boolean matched = false;
                String name = recipeFull.recipe.getName().toLowerCase();

                if (name.contains(searchTerm.toLowerCase())) {
                    matched = true;
                }

                if (matched) {
                    matchedRecipes.add(recipeFull);
                }

            }

            return matchedRecipes;
        }

        return recipes;
    }

}
