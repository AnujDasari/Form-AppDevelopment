package com.anuj.task1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anuj on 12-10-2016.
 */
public class SQLiteListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> mFirstName_arrayList;
    private ArrayList<String> mLastName_arrayList;


    public SQLiteListAdapter(Context context, ArrayList<String> firstName_arrayList, ArrayList<String> lastName_arrayList) {

        this.mContext = context;

        this.mFirstName_arrayList = firstName_arrayList;

        this.mLastName_arrayList = lastName_arrayList;


    }

    @Override
    public int getCount() {

        return mFirstName_arrayList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;

        LayoutInflater layoutInflater;

        if (convertView == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_view_data_layout, null);

            holder = new Holder();

            holder.textviewfirstname = (TextView) convertView.findViewById(R.id.textViewFirstName);

            holder.textviewlastname = (TextView) convertView.findViewById(R.id.textViewLastName);

            holder.imageView = (ImageView) convertView.findViewById(R.id.arrowImage);

            convertView.setTag(holder);

        } else {

            holder = (Holder) convertView.getTag();
        }


        holder.textviewfirstname.setText(mFirstName_arrayList.get(position));

        holder.textviewlastname.setText(mLastName_arrayList.get(position));

        return convertView;
    }

    public class Holder {
        TextView textviewfirstname;

        TextView textviewlastname;

        ImageView imageView;

    }
}
