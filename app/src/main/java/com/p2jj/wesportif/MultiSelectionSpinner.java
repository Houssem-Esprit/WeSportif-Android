package com.p2jj.wesportif;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import androidx.appcompat.widget.AppCompatSpinner;

import com.p2jj.wesportif.Activities.SignUpActivity;
import com.p2jj.wesportif.Model.CategorieSport;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiSelectionSpinner extends AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener {

    ArrayList<CategorieSport> items = null;
    boolean[] selection = null;
    ArrayAdapter adapter;


    public MultiSelectionSpinner(Context context) {
        super(context);
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);

        super.setAdapter(adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        adapter = new ArrayAdapter(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(adapter);
    }

    @Override
    public void onClick(DialogInterface dialog, int idx, boolean isChecked) {

        if (selection != null && idx < selection.length) {
            selection[idx] = isChecked;

            adapter.clear();
            adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "'idx' is out of bounds.");
        }

    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] itemNames = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            itemNames[i] = items.get(i).getNameSport();
        }

        builder.setMultiChoiceItems(itemNames, selection, this);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                // Do nothing
            }
        });

        builder.show();

        return true;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {


    }

    public void setItems(ArrayList<CategorieSport> items) {
        this.items = items;
        selection = new boolean[this.items.size()];
        adapter.clear();
        adapter.add("Choisir la catégorie de sport ?"+"");
        Arrays.fill(selection, false);
    }

    public void setSelection(ArrayList<CategorieSport> selection) {
        for (int i = 0; i < this.selection.length; i++) {
            this.selection[i] = false;
        }

        for (CategorieSport sel : selection) {
            for (int j = 0; j < items.size(); ++j) {
                if (items.get(j).getValue().equals(sel.getValue())) {
                    this.selection[j] = true;
                }
            }
        }

        adapter.clear();
        adapter.add(buildSelectedItemString());
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < items.size(); ++i) {
            if (selection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }

                foundOne = true;

                sb.append(items.get(i).getNameSport());
            }
        }

        return sb.toString();
    }


    public ArrayList<CategorieSport> getSelectedItems() {
        ArrayList<CategorieSport> selectedItems = new ArrayList<>();

        for (int i = 0; i < items.size(); ++i) {
            if (selection[i]) {
                selectedItems.add(items.get(i));
            }
        }

        return selectedItems;
    }
}
