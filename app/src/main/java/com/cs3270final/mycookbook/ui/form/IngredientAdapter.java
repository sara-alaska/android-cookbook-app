package com.cs3270final.mycookbook.ui.form;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.cs3270final.mycookbook.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<IngredientInputView> ingredients = new ArrayList<>();

    public List<IngredientInputView> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientInputView> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
        notifyDataSetChanged();
    }

    // Adds an ingredient and turns off the add button on the previous ingredient.
    public void addIngredient(IngredientInputView ingredient) {

        // Gets the count of all ingredients
        Integer itemCount = getItemCount();

        // If there are any ingredients remove the plus button from the last ingredient
        if (itemCount > 0) {
            ingredients.get(itemCount - 1).showAddButton = false;
            notifyItemChanged(itemCount - 1);
        }
        // Add a new ingredient with the add button (showAddbutton defaults to true)
        ingredients.add(ingredient);
        notifyItemInserted(getItemCount() - 1);
    }

    public void updateIngredient(IngredientInputView ingredient) {
        Integer item = ingredients.indexOf(ingredient);
        ingredients.set(item, ingredient);
        notifyItemChanged(item);
    }

    public interface IngredientListener {
        void addNewIngredient();
    }

    public static IngredientListener ingredientListener;

    public IngredientAdapter(Fragment context) {
        ingredientListener = (IngredientListener) context;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_input_item, parent, false));
        vh.attachListeners();
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        holder.bindItem(ingredients.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText name;
        private TextInputEditText amount;
        private MaterialTextView unit;
        private ImageView addNewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ingredient_name_input);
            amount = itemView.findViewById(R.id.ingredient_amount_input);
            unit = itemView.findViewById(R.id.ingredient_unit_input);
            addNewButton = itemView.findViewById(R.id.add_ingredient_button);
        }

        public void attachListeners() {
            name.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Intentionally left blank.
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ingredients.get(getAdapterPosition()).setName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Intentionally left blank.
                }

            });

            amount.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Intentionally left blank.
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try {
                        ingredients.get(getAdapterPosition()).setAmount(Float.parseFloat(s.toString()));
                    } catch (Exception e) {
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Intentionally left blank.
                }
            });
        }

        public void bindItem(IngredientInputView ingredient) {
            name.setText(ingredient.getName());
            unit.setText(ingredient.getUnit());

            if (ingredient.getAmount() != null) {
                amount.setText(String.format(Locale.US, "%.3f", ingredient.getAmount()));
            } else {
                amount.setText(null);
            }

            unit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ingredient.unitDialog.show();
                }

            });

            if (ingredient.showAddButton) {
                addNewButton.setVisibility(View.VISIBLE);
            } else {
                addNewButton.setVisibility(View.INVISIBLE);
            }

            addNewButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ingredientListener.addNewIngredient();
                }

            });

        }

    }
}
