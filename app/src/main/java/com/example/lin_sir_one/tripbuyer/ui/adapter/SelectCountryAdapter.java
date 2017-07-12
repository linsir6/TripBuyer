package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/6.选择国家的适配器
 */
public class SelectCountryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<String> mList;
    private int DEFAULT = 0;

    public SelectCountryAdapter() {
        mList = new ArrayList<>();
    }

    public void refresh(List<String> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_country, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((AddressItemViewHolder) holder, mList.get(position));

    }

    private void bindView(AddressItemViewHolder holder, String data) {
        String address;
        holder.countryName.setText(data);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class AddressItemViewHolder extends RecyclerView.ViewHolder {
        private TextView countryName;

        public AddressItemViewHolder(View itemView) {
            super(itemView);
            countryName = (TextView) itemView.findViewById(R.id.item_country_name);
        }
    }
}

