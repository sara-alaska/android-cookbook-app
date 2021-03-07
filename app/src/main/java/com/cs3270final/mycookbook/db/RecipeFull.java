package com.cs3270final.mycookbook.db;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class RecipeFull {

    @Embedded
    public Recipe recipe;

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Ingredient> ingredientList;

    @Relation(parentColumn = "id", entityColumn = "recipe_id")
    public List<Instruction> instructionList;

    @Relation(
        parentColumn = "id",
        entity = Tag.class,
        entityColumn = "id",
        associateBy = @Junction(
            value = RecipeTag.class,
            parentColumn = "recipe_id",
            entityColumn = "tag_id")
        )
    public List<Tag> tagList;

}
