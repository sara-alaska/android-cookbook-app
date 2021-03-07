package com.cs3270final.mycookbook.ui.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import com.cs3270final.mycookbook.R;
import com.cs3270final.mycookbook.db.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private List<Instruction> instructionItemList = new ArrayList<>();

    public void setInstructionItems(List<Instruction> recipeInputItemList) {
        this.instructionItemList = recipeInputItemList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InstructionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.ViewHolder holder, int position) {
        holder.bindItem(instructionItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return instructionItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void bindItem(Instruction instructionItem) {
            MaterialTextView stepNumber = itemView.findViewById(R.id.instruction_step_number_view);
            stepNumber.setText(Integer.toString(instructionItem.getStepNumber()));
            MaterialTextView instructionStep = itemView.findViewById(R.id.instruction_step_view);
            instructionStep.setText(instructionItem.getStepName());
        }
    }
}
