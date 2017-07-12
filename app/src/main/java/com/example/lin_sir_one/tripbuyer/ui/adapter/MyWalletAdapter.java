package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.WalletLogModel;
import com.example.lin_sir_one.tripbuyer.utils.DateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/11.钱包交易明细的适配器
 */
public class MyWalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<WalletLogModel> mList;
    private int DEFAULT = 0;

    public MyWalletAdapter() {
        mList = new ArrayList<>();
    }

    public void refresh(List<WalletLogModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wallet_log, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((MyWalletLogViewHolder) holder, mList.get(position));

    }

    private void bindView(MyWalletLogViewHolder holder, WalletLogModel data) {
        if (data.getSymbol().equals("+")) {
            holder.shouRu.setText("收入");
        } else {
            holder.shouRu.setText("支出");
        }

        holder.shiJian.setText(DateUtil.longToDate(data.getCreateDate()));
        holder.qian.setText(data.getSum() + " 元");

        try {

            Log.i("lin", "---linSir---> 1111");
            //holder.qian.setText(data.getSum() + ".00 元");
            String money = data.getSum();
            float scale = Float.valueOf(money);
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String dd = fnum.format(scale);
            holder.qian.setText(dd + " 元");
        } catch (Exception e) {
            Log.i("lin", "---linSir---> 5555");
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyWalletLogViewHolder extends RecyclerView.ViewHolder {
        private TextView shouRu;
        private TextView shiJian;
        private TextView qian;

        public MyWalletLogViewHolder(View itemView) {
            super(itemView);
            shouRu = (TextView) itemView.findViewById(R.id.shou_ru);
            shiJian = (TextView) itemView.findViewById(R.id.ri_qi);
            qian = (TextView) itemView.findViewById(R.id.qian);
        }
    }
}
