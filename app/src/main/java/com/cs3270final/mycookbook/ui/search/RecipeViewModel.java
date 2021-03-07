package com.cs3270final.mycookbook.ui.search;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.RecipeFull;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    public LiveData<List<RecipeFull>> recipes;

    public LiveData<List<RecipeFull>> getAllSortedByDateDesc(Context context) {

        AppDatabase db = AppDatabase.getInstance(context);
        recipes = db.recipeDAO().selectAllByDateDesc();

        return recipes;

    }
}
