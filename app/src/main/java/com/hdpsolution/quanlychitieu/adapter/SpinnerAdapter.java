package com.hdpsolution.quanlychitieu.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdpsolution.quanlychitieu.model.ListItemAddProg;
import com.hdpsolution.quanlychitieu.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<ListItemAddProg> {
    LayoutInflater inflater;
    ArrayList<ListItemAddProg> objects;
    ViewHolder holder = null;

    public SpinnerAdapter(Context context, int textViewResourceId, ArrayList<ListItemAddProg> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        ListItemAddProg listItemAddProg = objects.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.spinner_row, parent, false);
            holder.name = (TextView) row.findViewById(R.id.spinner_name);
            holder.img = (ImageView) row.findViewById(R.id.spinner_img);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.name.setText(listItemAddProg.getName());
        holder.img.setBackgroundResource(listItemAddProg.getLogo());
        return row;
    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }
}