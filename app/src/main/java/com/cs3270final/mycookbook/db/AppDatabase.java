package com.cs3270final.mycookbook.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Recipe.class, Ingredient.class, Instruction.class, Tag.class, RecipeTag.class, ShoppingListItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if(instance != null)
            return instance;
        instance = Room.databaseBuilder(context, AppDatabase.class, "recipes-database").build();
        return instance;
    }

    public abstract RecipeDAO recipeDAO();

    public abstract IngredientDAO ingredientDAO();

    public abstract InstructionDAO instructionDAO();

    public abstract TagDAO tagDAO();

    public abstract TagRecipeDAO tagRecipeDAO();

    public abstract ShoppingListItemDAO shoppingListItemDAO();
}
