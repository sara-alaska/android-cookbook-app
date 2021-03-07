package com.cs3270final.mycookbook.ui.form;

import android.app.AlertDialog;

import com.cs3270final.mycookbook.db.Ingredient;

import java.util.function.Function;

// Used for displaying an ingredient
public class IngredientInputView extends Ingredient {
    public boolean showAddButton = true;
    public AlertDialog unitDialog;
}
