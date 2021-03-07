package com.cs3270final.mycookbook.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingListItem {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ForeignKey
            (entity = Ingredient.class,
                    parentColumns = "id",
                    childColumns = "ingredient_id"
            )
    @ColumnInfo(name = "ingredient_id")
    private Long ingredient_id;

    @ColumnInfo(name = "is_checked")
    private Boolean is_checked;

    public ShoppingListItem(Long ingredient_id, Boolean is_checked) {

        this.ingredient_id = ingredient_id;
        this.is_checked = is_checked;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIngredient_id() {
        return ingredient_id;
    }

    public void setIngredient_id(Long ingredient_id) {
        this.ingredient_id = ingredient_id;
    }

    public Boolean getIs_checked() {
        return is_checked;
    }

    public void setIs_checked(Boolean is_checked) {
        this.is_checked = is_checked;
    }

}
