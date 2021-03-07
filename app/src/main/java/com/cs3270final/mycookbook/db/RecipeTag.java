package com.cs3270final.mycookbook.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(primaryKeys = {"recipe_id", "tag_id"})
public class RecipeTag {

    @ForeignKey
        (entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipe_id"
        )
    @ColumnInfo(name = "recipe_id")
    @NonNull
    public Long recipe_id;

    @ForeignKey
        (entity = Tag.class,
            parentColumns = "id",
            childColumns = "tag_id"
        )
    @ColumnInfo(name = "tag_id")
    @NonNull
    public Long tag_id;

    public RecipeTag(@NonNull Long recipe_id, @NonNull Long tag_id) {
        this.recipe_id = recipe_id;
        this.tag_id = tag_id;
    }

}
