package com.cs3270final.mycookbook.ui.shoppinglist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.AppDatabase;
import com.cs3270final.mycookbook.db.ShoppingListItemFull;

import java.util.List;

public class ShoppingListFragment extends Fragment {

    private View root;
    public ShoppingListItemAdapter listAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        initShoppingListRecyclerView();
        initShoppingListItems();
    }

    private void initShoppingListItems() {
        new ViewModelProvider(requireActivity()).get(ShoppingListViewModel.class).getAll(getContext())
            .observe(this, new Observer<List<ShoppingListItemFull>>() {

                @Override
                public void onChanged(List<ShoppingListItemFull> shoppingListItemFulls) {

                    if (shoppingListItemFulls != null) {
                        listAdapter.setShoppingListItems(shoppingListItemFulls);
                    }

                }

            });
    }


    private void initShoppingListRecyclerView() {
        RecyclerView listRecyclerView = root.findViewById(R.id.shopping_list_recycler_view);
        listAdapter = new ShoppingListItemAdapter();
        listRecyclerView.setAdapter(listAdapter);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        // Deletes the item in swipe
        new ItemTouchHelper(new ShoppingListTouchHelperCallback(0, ItemTouchHelper.LEFT)).attachToRecyclerView(listRecyclerView);

    }

    public void removeShoppingListItem(int position) {
        listAdapter.removeShoppingListItem(position);
        ShoppingListItemFull item = listAdapter.getShoppingListItem(position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getContext());
                db.shoppingListItemDAO().delete(item.shoppingListItem);
            }
        }).start();
    }

    // Handles swiping.
    class ShoppingListTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        public ShoppingListTouchHelperCallback(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            removeShoppingListItem(viewHolder.getAdapterPosition());
        }
    }

}
