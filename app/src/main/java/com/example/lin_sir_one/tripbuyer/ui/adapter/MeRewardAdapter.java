package com.example.lin_sir_one.tripbuyer.ui.adapter;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/21.我的悬赏令的适配器
 */
public class MeRewardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<FamousPageModel> mList;

    public MeRewardAdapter() {
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

        try {
            holder.productName.setText(data.getProductName());
            holder.productPrice.setText(data.getTotal());
            holder.productCount.setText(data.getNum());
            ImageUtil.requestImg(BaseApplication.get().getAppContext(), data.getPicture().split(";")[0], holder.productImg);
            //holder.productHint.setText("已接单");

            if (data.getStatus().equals("dzf")){
                holder.productHint.setText("已取消悬赏");
            }else if (data.getStatus().equals("yzf")){
                holder.productHint.setText("正在悬赏");
            }else if (data.getStatus().equals("jyz")||data.getStatus().equals("jywc")){
                holder.productHint.setText("悬赏成功");
            }



        } catch (Exception e) {

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
















