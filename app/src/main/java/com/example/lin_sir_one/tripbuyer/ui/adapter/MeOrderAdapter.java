package com.example.lin_sir_one.tripbuyer.ui.adapter;

/**
 * Created by linSir on 16/9/9.我的订单的界面
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/21.我的悬赏令的适配器
 */
public class MeOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<FamousPageModel> mList;

    public MeOrderAdapter() {
        mList = new ArrayList<>();
    }

    public void refresh(List<FamousPageModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me_reward, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((MyWalletLogViewHolder) holder, mList.get(position));

    }

    private void bindView(MyWalletLogViewHolder holder, FamousPageModel data) {

//        未发货：yzf  yzjpxx
//        已发货：jyz
//        已完成：jywc
//        订单取消：qx

        try {
            holder.productName.setText(data.getProductName());
            holder.productPrice.setText("￥ " + data.getTotal());
            holder.productCount.setText("x " + data.getNum());
            ImageUtil.requestImg(BaseApplication.get().getAppContext(), data.getPicture().split(";")[0], holder.productImg);
            //holder.productHint.setText("已接单");

            KLog.i("----lin----> " + data.getStatus());

            if (data.getStatus().equals("yzf") || data.getStatus().equals("yzjpxx") || data.getStatus().equals("cj")) {
                KLog.i("----lin----> 111111111111");
                holder.productHint.setText("未发货");
            } else if (data.getStatus().equals("jyz")) {
                KLog.i("----lin----> 2222222222222");
                holder.productHint.setText("已发货");
            } else if (data.getStatus().equals("jywc")) {
                KLog.i("----lin----> 3333333333333");
                holder.productHint.setText("已完成");
            } else if (data.getStatus().equals("qx")) {
                KLog.i("----lin----> 44444444444444");
                holder.productHint.setText("订单已取消");
            }


        } catch (Exception e) {
            KLog.i("----lin----> 555555555555555");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyWalletLogViewHolder extends RecyclerView.ViewHolder {

        private TextView productName, productHint, productCount, productPrice;
        private ImageView productImg;

        public MyWalletLogViewHolder(View itemView) {
            super(itemView);

            productName = (TextView) itemView.findViewById(R.id.tv_product_name);
            productHint = (TextView) itemView.findViewById(R.id.tv_product_hint);
            productCount = (TextView) itemView.findViewById(R.id.tv_product_count);
            productPrice = (TextView) itemView.findViewById(R.id.tv_product_price);

            productImg = (ImageView) itemView.findViewById(R.id.iv_product);


        }
    }
}




