package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.AllAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/7/24.管理地址界面的适配器
 */
public class ManageAddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public OnTitleClickListener mListener;
    private List<AllAddress> mList;  //用户列表

    public ManageAddressAdapter() {
        mList = new ArrayList<>();
    }

    public void setList(List<AllAddress> list) {
        mList.clear();
        mList.addAll(list);
        mList = list;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ManageAddressViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manage_address, parent, false));
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ManageAddressViewHolder mHolder = (ManageAddressViewHolder) holder;
        mHolder.userName.setText(mList.get(position).getShipName());
        mHolder.userTel.setText(mList.get(position).getPhone());

        String address = mList.get(position).getProvince() + " " + mList.get(position).getCity()
                + " " + mList.get(position).getArea() + " " + mList.get(position).getDetail();
        mHolder.address.setText(address);



//        mHolder._default.setOnClickListener(new ClickListener(String.valueOf(position)));

    }

    @Override public int getItemCount() {
        return mList.size();
    }


    public static class ManageAddressViewHolder extends RecyclerView.ViewHolder {

        private TextView userName;
        private TextView userTel;
        private TextView address;
        private TextView _default;
        private CheckBox checkBox;

        public ManageAddressViewHolder(View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.manage_userName);
            userTel = (TextView) itemView.findViewById(R.id.manage_userTel);
            address = (TextView) itemView.findViewById(R.id.manage_userAddress);
            checkBox = (CheckBox) itemView.findViewById(R.id.manage_circle);
            // _default = (TextView) itemView.findViewById(R.id.manage_default);


        }
    }


    public class ClickListener implements View.OnClickListener {
        private String id;

        public ClickListener(String id) {
            this.id = id;
        }

        @Override public void onClick(View view) {
            if (mListener != null) {
                mListener.onTitleClick(id);
            }
        }
    }


    public void setOnTitleClickListener(OnTitleClickListener listener) {
        mListener = listener;
    }


    public interface OnTitleClickListener {
        void onTitleClick(String id);
    }


}











