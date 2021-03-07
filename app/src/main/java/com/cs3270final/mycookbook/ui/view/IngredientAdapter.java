package com.cs3270final.mycookbook.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredientItemList = new ArrayList<>();

    public void setIngredients(List<Ingredient> recipeInputItemList) {
        this.ingredientItemList = recipeInputItemList;
        notifyDataSetChanged();
    }

    public interface CartButtonHandler {
        void addToShoppingList(Ingredient ingredient);
    }

    private CartButtonHandler cartButtonHandler;

    public IngredientAdapter(Fragment context) {
        cartButtonHandler = (CartButtonHandler) context;
    }

    @NonNull
    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IngredientAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.ViewHolder holder, int position) {
        holder.bindItem(ingredientItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return ingredientItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MaterialTextView name;
        private MaterialTextView amount;
        private MaterialTextView unit;
        private ImageView cartIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.ingredient_name_view);
            amount = itemView.findViewById(R.id.ingredient_amount_view);
            unit = itemView.findViewById(R.id.ingredient_serving_view);
            cartIcon = itemView.findViewById(R.id.add_to_shopping_cart);
        }

        public void bindItem(Ingredient ingredientItem) {
            name.setText(ingredientItem.getName());
            amount.setText(itemView.getResources().getString(R.string.amt_format, ingredientItem.getAmount()));
            unit.setText(ingredientItem.getUnit());
            cartIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    cartButtonHandler.addToShoppingList(ingredientItemList.get(getAdapterPosition()));
                }

            });

        }
    }
}
