package com.cs3270final.mycookbook.db;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ShoppingListItemFull {

    @Embedded
    public ShoppingListItem shoppingListItem;

    @Relation(parentColumn = "ingredient_id", entityColumn = "id")
    public Ingredient ingredient;

}
