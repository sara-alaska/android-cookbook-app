package com.cs3270final.mycookbook.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Instruction {

    @PrimaryKey(autoGenerate = true)
    private Long id;

    @ColumnInfo(name = "step_number")
    private int stepNumber;

    @ColumnInfo(name = "step_name")
    private String stepName;

    @ForeignKey
        (entity = Recipe.class,
            parentColumns = "id",
            childColumns = "recipe_id"
        )
    private Long recipe_id;

    public Instruction() {}

    public Instruction(Long id, int stepNumber, String stepName, Long recipe_id) {
        this.id = id;
        this.stepNumber = stepNumber;
        this.stepName = stepName;
        this.recipe_id = recipe_id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Long getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Long recipe_id) {
        this.recipe_id = recipe_id;
    }

}
