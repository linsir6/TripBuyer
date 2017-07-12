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
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/6.所有订单界面的适配器
 */
public class AllOrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int FOOTER_TYPE = 0;//最后一个的类型
    public static final int HAS_IMG_TYPE = 1;//有图片的类型

    private List<FamousPageModel> dataList;

    private ProgressBar mProgress;
    private TextView mNoMore;

    public AllOrderAdapter() {
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
            return new AllAddressAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_order, parent, false));
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == FOOTER_TYPE) {
            bindFooterView((FooterItemViewHolder) holder);
        } else {

            if (dataList.get(position) != null) {
                bindView((AllAddressAdapterViewHolder) holder, dataList.get(position));
            }

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

    private void bindView(AllAddressAdapterViewHolder holder, FamousPageModel data) {

        ImageUtil.requestImg(BaseApplication.get().getAppContext(), data.getHead(), holder.img);
        KLog.i("====lin====>" + data.getHead());
        String[] products = data.getProductName().split(";");
        if (products.length == 1) {
            holder.p1.setText(products[0]);
            holder.p2.setVisibility(View.GONE);
            holder.p3.setVisibility(View.GONE);
        }
        if (products.length == 2) {
            holder.p1.setText(products[0]);
            holder.p2.setText(products[1]);
            holder.p3.setVisibility(View.GONE);
        }
        if (products.length == 3) {
            holder.p1.setText(products[0]);
            holder.p2.setText(products[1]);
            holder.p3.setText(products[2]);
        }
        holder.price.setText("￥：" + data.getTotal());
        holder.count.setText("数量：" + data.getNum());
        holder.where.setText("必须要有包装");

        switch (data.getDeliveryTime()) {

            case 5:
                holder.hint.setText("发货时间：五天之后发货");
                break;

            case 10:
                holder.hint.setText("发货时间：十天之后发货");
                break;

            case 15:
                holder.hint.setText("发货时间：十五天之后发货");
                break;

            default:
                holder.hint.setText("发货时间：不限制发货时间");
                break;
        }


    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 1;
    }

    public static class AllAddressAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private TextView p1, p2, p3;
        private TextView price;
        private TextView count;
        private TextView where;
        private TextView hint;

        public AllAddressAdapterViewHolder(View itemView) {
            super(itemView);

            img = (ImageView) itemView.findViewById(R.id.userInfo_all_order);
            p1 = (TextView) itemView.findViewById(R.id.p1_all_order);
            p2 = (TextView) itemView.findViewById(R.id.p2_all_order);
            p3 = (TextView) itemView.findViewById(R.id.p3_all_order);
            price = (TextView) itemView.findViewById(R.id.price_all_order);
            count = (TextView) itemView.findViewById(R.id.count_all_order);
            where = (TextView) itemView.findViewById(R.id.where_all_order);
            hint = (TextView) itemView.findViewById(R.id.hint_all_order);

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
