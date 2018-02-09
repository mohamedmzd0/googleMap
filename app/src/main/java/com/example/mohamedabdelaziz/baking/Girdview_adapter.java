package com.example.mohamedabdelaziz.baking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mohamed Abd ELaziz on 6/28/2017.
 */

public class Girdview_adapter extends BaseAdapter {
    Context context ;
    ArrayList<recipe>recipeArrayList;

    public Girdview_adapter(Context context, ArrayList<recipe> recipeArrayList) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
    }

    @Override
    public int getCount() {
        return recipeArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return recipeArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView= layoutInflater.inflate(R.layout.recipes_items,null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.recip_image);
        TextView textView= (TextView) convertView.findViewById(R.id.recipe_name);

        try {
            Picasso.with(context).load(recipeArrayList.get(position).getImage()).into(imageView);
        }catch (Exception e)
        {
            imageView.setImageResource(R.drawable.food);
        }
        textView.setText(recipeArrayList.get(position).getName());
        return convertView;
    }
}
