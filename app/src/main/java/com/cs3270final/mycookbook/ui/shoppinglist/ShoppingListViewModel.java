package com.cs3270final.mycookbook.ui.shoppinglist;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.ShoppingListItemFull;

import java.util.List;

public class ShoppingListViewModel extends ViewModel {

    public LiveData<List<ShoppingListItemFull>> shoppingListItems;

    public LiveData<List<ShoppingListItemFull>> getAll(Context context) {

        AppDatabase db = AppDatabase.getInstance(context);
        shoppingListItems = db.shoppingListItemDAO().selectAll();

        return shoppingListItems;
    }
}
