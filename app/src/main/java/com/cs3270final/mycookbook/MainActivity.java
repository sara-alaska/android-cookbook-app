package com.cs3270final.mycookbook;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.cs3270final.mycookbook.ui.form.FormFragment;
import com.cs3270final.mycookbook.ui.search.RecipeAdapter;
import com.cs3270final.mycookbook.ui.search.SearchFragment;
import com.cs3270final.mycookbook.ui.view.ViewFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ViewRecipeFragmentLoader, SearchFragment.KeyboardManipulator, SearchFragment.FragmentLoader, ViewFragment.FragmentLoader, FormFragment.FormAndKeyboardHandler {

    public static final String BUNDLE_KEY_RECIPE_ID = "recipe_id";
    private NavController navController;
    private AppBarConfiguration appBarConfiguration;
    public Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView navView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_search, R.id.navigation_shopping_list)
            .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    // Allows back button to work
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp();
    }

    @Override
    public void viewRecipe(Long recipeId) {
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_KEY_RECIPE_ID, recipeId);
        navController.navigate(R.id.navigation_view_fragment, bundle);
    }

    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
    }

    public void showKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void navigateToNewRecipeForm() {
        navController.navigate(R.id.navigation_form);
    }

    @Override
    public void navigateToEditRecipeForm(Long recipeId) {
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_KEY_RECIPE_ID, recipeId);
        navController.navigate(R.id.navigation_form, bundle);
    }

    public void navigateToSearch() {
        navController.navigate(R.id.navigation_search);
        Toast.makeText(this, getResources().getString(R.string.recipe_saved_message), Toast.LENGTH_SHORT).show();
    }
}