package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/7/18.地址界面的适配器
 */
public class ShippingAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<AllAddress> mList;
    private int DEFAULT = 0;

    public ShippingAddressAdapter() {
        mList = new ArrayList<>();
    }

    public void refresh(List<AllAddress> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddressItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shipping_address, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((AddressItemViewHolder) holder, mList.get(position));

    }


    private void bindView(AddressItemViewHolder holder, AllAddress data) {
        String address;
        holder.tvUserName.setText(data.getShipName());
        holder.tvUserTel.setText(data.getPhone());
        if (DEFAULT != 0) {
            holder.tvUserDefault.setVisibility(View.GONE);
            address = data.getProvince() + " " + data.getCity() +
                    " " + data.getArea() + " " + data.getDetail();
        }else{
            holder.tvUserDefault.setVisibility(View.VISIBLE);
            address = "                      " + data.getProvince() + " " + data.getCity() +
                    " " + data.getArea() + " " + data.getDetail();

        }
        DEFAULT=1;

        holder.tvUserAddress.setText(address);


    }


    @Override
    public int getItemCount() {
        return mList.size();
    }


    public static class AddressItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName;
        private TextView tvUserTel;
        private TextView tvUserDefault;
        private TextView tvUserAddress;

        public AddressItemViewHolder(View itemView) {
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tv_username_item);
            tvUserTel = (TextView) itemView.findViewById(R.id.tv_tel_item);
            tvUserDefault = (TextView) itemView.findViewById(R.id.tv_default_item);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_shipping_address_item);
        }
    }

}
