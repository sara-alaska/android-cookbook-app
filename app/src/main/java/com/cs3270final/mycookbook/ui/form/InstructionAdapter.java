package com.cs3270final.mycookbook.ui.form;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import com.cs3270final.mycookbook.R;

import java.util.ArrayList;
import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.ViewHolder> {

    private List<InstructionInputView> instructions = new ArrayList<>();

    public List<InstructionInputView> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<InstructionInputView> instructions) {

        this.instructions.clear();
        this.instructions.addAll(instructions);
        notifyDataSetChanged();

    }

    public void addInstruction(InstructionInputView instruction) {
        Integer itemCount = getItemCount();

        if (itemCount > 0) {
            instructions.get(itemCount - 1).showAddButton = false;
            notifyItemChanged(itemCount - 1);
        }

        this.instructions.add(instruction);
        notifyItemInserted(getItemCount() - 1);
    }

    public interface InstructionListener {
        void addNewInstruction();
    }

    private static InstructionListener instructionListener;

    public InstructionAdapter(Fragment context) {
        instructionListener = (InstructionListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.instruction_input_item, parent, false));
        vh.attachListeners();

        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull InstructionAdapter.ViewHolder holder, int position) {
        holder.bindItem(instructions.get(position));
    }

    @Override
    public int getItemCount() {
        return instructions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView stepNumber;
        private TextInputEditText description;
        private ImageView addNewButton;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            stepNumber = itemView.findViewById(R.id.instruction_step_number);
            description = itemView.findViewById(R.id.instruction_input);
            addNewButton = itemView.findViewById(R.id.add_instruction_button);
        }

        public void attachListeners() {
            description.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Intentionally left blank.
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    instructions.get(getAdapterPosition()).setStepName(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Intentionally left blank.
                }
            });
        }

        public void bindItem(InstructionInputView instruction) {

            // Setting the step number to a position number given by the adapter and adding one
            // Because step numbers should start at 1
            stepNumber.setText(Integer.toString(getAdapterPosition() + 1));
            description.setText(instruction.getStepName());

            if (instruction.showAddButton) {
                addNewButton.setVisibility(View.VISIBLE);
            } else {
                addNewButton.setVisibility(View.INVISIBLE);
            }

            addNewButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    instructionListener.addNewInstruction();
                }

            });

        }
    }
}