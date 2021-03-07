package com.cs3270final.mycookbook.ui.shoppinglist;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.ShoppingListItemFull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListItemAdapter extends RecyclerView.Adapter<ShoppingListItemAdapter.ViewHolder> {

    private List<ShoppingListItemFull> shoppingListItems = new ArrayList<>();

    public void setShoppingListItems(List<ShoppingListItemFull> shoppingListItems) {
        this.shoppingListItems = shoppingListItems;
        notifyDataSetChanged();
    }

    public void removeShoppingListItem(int position) {
        this.shoppingListItems.remove(position);
        notifyItemRemoved(position);
    }

    public ShoppingListItemFull getShoppingListItem(int position) {
        return shoppingListItems.get(position);
    }

    @NonNull
    @Override
    public ShoppingListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ShoppingListItemAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListItemAdapter.ViewHolder holder, int position) {
        holder.bindItem(shoppingListItems.get(position));
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckBox checkbox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkbox = itemView.findViewById(R.id.checkbox_shopping_list);

        }

        public void bindItem(ShoppingListItemFull shoppingListItem) {
            checkbox.setChecked(shoppingListItem.shoppingListItem.getIs_checked());
            checkbox.setText(shoppingListItem.ingredient.getName());
        }
    }
}
