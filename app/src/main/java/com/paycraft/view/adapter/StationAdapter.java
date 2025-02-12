package com.paycraft.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.paycraft.model.beans.Station;
import com.paycraft.ticketbookingoperator.R;

import java.util.ArrayList;
import java.util.List;


public class StationAdapter extends ArrayAdapter {

    private List<Station> dataList;
    private final int itemLayout;
    private final ListFilter listFilter = new ListFilter();
    private List<Station> dataListAllItems;

    public StationAdapter(Context context, int resource, List<Station> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).getName());
        return dataList.get(position).getName();
    }

    public Station getStation(int position){
        return dataList.get(position);
    }


    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = view.findViewById(R.id.lbl_name);
        strName.setText(getItem(position));
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    class ListFilter extends Filter {
        private final Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<Station> matchValues = new ArrayList<>();

                for (Station dataItem : dataListAllItems) {
                    if (dataItem.getName().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Station>) results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}