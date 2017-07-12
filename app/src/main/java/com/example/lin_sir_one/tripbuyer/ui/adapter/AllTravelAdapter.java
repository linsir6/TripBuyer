package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.app.BaseApplication;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.utils.DateUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/7/18.所有行程界面的适配器
 */
public class AllTravelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int FOOTER_TYPE = 0;//最后一个的类型
    public static final int HAS_IMG_TYPE = 1;//有图片的类型

    private List<JourneyModel> dataList;

    private ProgressBar mProgress;
    private TextView mNoMore;

    public AllTravelAdapter() {
        dataList = new ArrayList<>();
    }

    public void addData(List<JourneyModel> list) {
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER_TYPE) {
            return new FooterItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer, parent, false));
        } else {
            return new AllAddressAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_all_address, parent, false));
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
            JourneyModel news = dataList.get(position);
            return HAS_IMG_TYPE;
        }
    }

    private void bindView(AllAddressAdapterViewHolder holder, JourneyModel data) {

        try {
            ImageUtil.requestCircleImg(BaseApplication.get().getAppContext(), data.getUser().getHead(), holder.userInfo);
            String place = "旅行地点： " + data.getTravelPlace() + "-" + data.getMainCity();
            holder.tvUserAddress.setText(place);

            String begin = "出行时间： " + DateUtil.longToDate(data.getTravelDate());
            holder.tvUserBegin.setText(begin);

            String end = "返程时间： " + DateUtil.longToDate(data.getReturnDate());
            Log.i("lin", "----lin----> 返程时间" + end);

            holder.tvUserEnd.setText(end);

            holder.tvUserName.setText(data.getUser().getName());


        } catch (Exception e) {

        }


    }


    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size() + 1;
    }

    public static class AllAddressAdapterViewHolder extends RecyclerView.ViewHolder {

        private ImageView userInfo;
        private TextView tvUserName;
        private TextView tvUserBegin;
        private TextView tvUserEnd;
        private TextView tvUserAddress;

        public AllAddressAdapterViewHolder(View itemView) {
            super(itemView);

            userInfo = (ImageView) itemView.findViewById(R.id.user_info_all_address);
            tvUserName = (TextView) itemView.findViewById(R.id.tv_userName_all_address);
            tvUserBegin = (TextView) itemView.findViewById(R.id.tv_begin_all_address);
            tvUserEnd = (TextView) itemView.findViewById(R.id.tv_end_time_all_address);
            tvUserAddress = (TextView) itemView.findViewById(R.id.tv_place_all_address);
        }
    }

    /**
     * 刷新列表
     */
    public void refreshList(List<JourneyModel> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 加载更多
     */
    public void loadMoreList(List<JourneyModel> list) {
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
    public JourneyModel getClickItem(int position) {
        if (position < dataList.size()) {
            return dataList.get(position);
        } else {
            return null;
        }
    }


}
