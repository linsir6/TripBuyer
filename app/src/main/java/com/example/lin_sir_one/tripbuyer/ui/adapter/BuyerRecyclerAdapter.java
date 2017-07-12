package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lin_sir on 2016/7/7.部分商品的展示,的适配器
 */
public class BuyerRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int FOOTER_TYPE = 0;//最后一个的类型
    public static final int HAS_IMG_TYPE = 1;//有图片的类型

    private List<FamousPageModel> dataList;

    private ProgressBar mProgress;
    private TextView mNoMore;

    public BuyerRecyclerAdapter() {
        dataList = new ArrayList<>();
    }

    public void addData(List<FamousPageModel> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            return new FooterItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
        } else {
            return new BuyerItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_buyer, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == FOOTER_TYPE) {
            bindFooterView((FooterItemViewHolder) holder);
        } else {
            bindView((BuyerItemViewHolder) holder, dataList.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return FOOTER_TYPE;
        } else {
            FamousPageModel news = dataList.get(position);
            return HAS_IMG_TYPE;
        }
    }

    private void bindView(BuyerItemViewHolder holder, FamousPageModel data) {
        try {


            String productName = data.getProductName();
            String[] products = productName.split(";");
            if (products.length != 1) {
                productName = products[1] + " 等商品";
            }

            String count = data.getNum();
            int sum = 0;
            String[] counts = count.split(";");
            if (counts.length == 2) {
                sum = Integer.parseInt(counts[0]) + Integer.parseInt(counts[1]);
                count = String.valueOf(sum);
            }

            if (counts.length == 3) {
                sum = Integer.parseInt(counts[0]) + Integer.parseInt(counts[1]) + Integer.parseInt(counts[2]);
                count = String.valueOf(sum);
            }

            holder.count.setText(count);
            holder.goods_name.setText(productName);
            holder.price.setText(data.getTotal());
            if (data.getUser() != null) {
                holder.user_name.setText(data.getUser().getName());

            }

            String imgUrl = data.getPicture();

            if (imgUrl != null) {
                ImageUtil.requestImg(BaseApplication.get().getAppContext(), imgUrl, holder.img);
            }
        } catch (Exception e) {

        }
    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 1;
    }

    public static class BuyerItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView price;
        private TextView goods_name;
        private TextView count;
        private TextView user_name;


        public BuyerItemViewHolder(View itemView) {

            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.iv_user_item_buyer2);
            price = (TextView) itemView.findViewById(R.id.tv_product_price);
            goods_name = (TextView) itemView.findViewById(R.id.tv_product_name);
            count = (TextView) itemView.findViewById(R.id.tv_product_number);
            user_name = (TextView) itemView.findViewById(R.id.tv_user_name);

        }
    }


    /**
     * 刷新列表
     */

    public void refreshList(List<FamousPageModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多
     */
    public void loadMoreList(List<FamousPageModel> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 显示没有更多
     */
    public void showNoMore() {
        if (getItemCount() > 0) {
            if (mProgress != null && mNoMore != null) {
                mNoMore.setVisibility(View.VISIBLE);
                mProgress.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 显示加载更多
     */
    public void showLoadMore() {
        if (mProgress != null && mNoMore != null) {
            mProgress.setVisibility(View.VISIBLE);
            mNoMore.setVisibility(View.GONE);
        }
    }

    private void bindFooterView(FooterItemViewHolder viewHolder) {
        mProgress = viewHolder.mProgress;
        mNoMore = viewHolder.tvNoMore;
    }


    public static class FooterItemViewHolder extends RecyclerView.ViewHolder {

        private ProgressBar mProgress;
        private TextView tvNoMore;

        public FooterItemViewHolder(View itemView) {
            super(itemView);
            mProgress = (ProgressBar) itemView.findViewById(R.id.pb_footer_load_more);
            tvNoMore = (TextView) itemView.findViewById(R.id.tv_footer_no_more);
        }
    }


    /**
     * 获取点击的 item,如果是最后一个,则返回 null
     */
    public FamousPageModel getClickItem(int position) {
        if (position < dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }

}
