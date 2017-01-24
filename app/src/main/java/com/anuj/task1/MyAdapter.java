package com.anuj.task1;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anuj on 04-10-2016.
 */
public class MyAdapter implements SpinnerAdapter {

    private ArrayList mNames;
    private Context mCtxt;
    private LayoutInflater mInflater;


    public MyAdapter(ArrayList names, Context ctxt) {
        this.mNames = names;
        this.mCtxt = ctxt;
        this.mInflater = (LayoutInflater) ctxt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {

        return mNames.size();
    }

    @Override
    public Object getItem(int position) {

        return mNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_view, parent, false);
        }

        TextView name = (TextView) convertView;
        name.setText(mNames.get(position).toString());
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.spinner_dropdown_item, parent, false);
        }
        TextView name = (TextView) convertView;
        name.setText(mNames.get(position).toString());
        return convertView;

    }
}
