package com.touchmenotapps.marketplace.common.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.touchmenotapps.marketplace.bo.CategoryDao;
import com.touchmenotapps.marketplace.common.interfaces.SubCategorySelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by i7 on 07-02-2018.
 */

public class SubCategorySelectionDialog {

    private Context context;
    private List<CategoryDao> subCategories = new ArrayList<>();
    private List<CategoryDao> subCategoriesFiltered = new ArrayList<>();
    private SubCategorySelectedListener subCategorySelectedListener;

    public SubCategorySelectionDialog(Context context, SubCategorySelectedListener subCategorySelectedListener) {
        this.subCategorySelectedListener = subCategorySelectedListener;
        this.context = context;
    }

    public void setCategoryListDao(List<CategoryDao> subCategories) {
        this.subCategories = subCategories;
    }

    public void setSubCategoriesFiltered(List<CategoryDao> subCategoriesFiltered) {
        this.subCategoriesFiltered = subCategoriesFiltered;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final String[] subCats = new String[subCategories.size()];
        final boolean[] checked = new boolean[subCategories.size()];

        for(int i = 0; i < subCategories.size(); i++) {
            subCats[i] = subCategories.get(i).getDescription();
            if(subCategoriesFiltered.contains(subCategories.get(i)))
                checked[i] = true;
            else
                checked[i] = false;
        }

        builder.setMultiChoiceItems(subCats, checked, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checked[which] = isChecked;
                if(isChecked) {
                    if(!subCategoriesFiltered.contains(subCategories.get(which))) {
                        subCategoriesFiltered.add(subCategories.get(which));
                    }
                } else {
                    subCategoriesFiltered.remove(subCategories.get(which));
                }
                Log.i("Test", subCats[which] + " " + isChecked);
            }
        });

        builder.setCancelable(false);
        builder.setTitle("Select Sub-Categories");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(subCategorySelectedListener != null) {
                    String names = "";
                    for(int i = 0; i < subCategoriesFiltered.size(); i++) {
                        if(i == 0) {
                            names += subCategoriesFiltered.get(i).getDescription();
                        } else {
                            names += ", " + subCategoriesFiltered.get(i).getDescription();
                        }
                    }
                    subCategorySelectedListener.onSubcategoriesSelected(names, subCategoriesFiltered);
                }
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
