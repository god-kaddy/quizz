package com.example.quizz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModel> cat_list;

    public CategoryAdapter(List<CategoryModel> cat_list) {
        this.cat_list = cat_list;
    }

    @Override
    public int getCount() {
        return cat_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View myView;

        if (view == null)
        {
            myView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cat_item_layout,viewGroup,false);
        }
        else
        {
            myView= view;
        }

        TextView catName=myView.findViewById(R.id.catName);
        TextView noOfTests= myView.findViewById(R.id.no_of_tests);

        catName.setText(cat_list.get(position).getName());
        noOfTests.setText(String.valueOf(cat_list.get(position).getNoOfTests()));

        return myView;
    }
}
