package com.cs3270final.mycookbook.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "amount")
    private Float amount;

    @ColumnInfo(name = "unit")
    private String unit;

    @ForeignKey
        (entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipe_id"
        )
    private Long recipe_id;

    public Ingredient() {}

    public Ingredient(Long id, String name, Float amount, String unit, Long recipe_id) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.unit = unit;
        this.recipe_id = recipe_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Long recipe_id) {
        this.recipe_id = recipe_id;
    }

}
