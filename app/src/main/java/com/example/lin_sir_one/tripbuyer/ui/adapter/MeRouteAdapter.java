package com.example.lin_sir_one.tripbuyer.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.utils.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linSir on 16/8/21.我的行程的适配器
 */
public class MeRouteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<JourneyModel> mList;
    private int DEFAULT = 0;

    public MeRouteAdapter() {
        mList = new ArrayList<>();
    }

    public void refresh(List<JourneyModel> list) {
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyWalletLogViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_me_route, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindView((MyWalletLogViewHolder) holder, mList.get(position));

    }

    private void bindView(MyWalletLogViewHolder holder, JourneyModel data) {

        String place = data.getTravelPlace();
        String mainCity = data.getMainCity();
        switch (place.split(";").length) {

            case 1:
                holder.t1.setText(place.split(";")[0] + " - " + mainCity.split(";")[0]);
                holder.t2.setVisibility(View.GONE);
                holder.t3.setVisibility(View.GONE);
                holder.i2.setVisibility(View.GONE);
                holder.i3.setVisibility(View.GONE);
                break;

            case 2:
                holder.t1.setText(place.split(";")[0] + " - " + mainCity.split(";")[0]);
                holder.t2.setText(place.split(";")[1] + " - " + mainCity.split(";")[1]);
                holder.t3.setVisibility(View.GONE);
                holder.i3.setVisibility(View.GONE);
                break;

            case 3:
                holder.t1.setText(place.split(";")[0]+ " - " + mainCity.split(";")[0]);
                holder.t2.setText(place.split(";")[1]+ " - " + mainCity.split(";")[1]);
                holder.t3.setText(place.split(";")[2]+ " - " + mainCity.split(";")[2]);
                break;
        }

        holder.time.setText(DateUtil.longToDate(data.getTravelDate()) + " - " + DateUtil.longToDate(data.getReturnDate()));
        long nowTime = System.currentTimeMillis();
        long backTime = Long.parseLong(data.getReturnDate());
        if (nowTime >= backTime) {
            holder.hint.setText("已出行");
        } else {
            holder.hint.setText("未出行");
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyWalletLogViewHolder extends RecyclerView.ViewHolder {
        private TextView t1, t2, t3;
        private ImageView i1, i2, i3;
        private TextView time, hint;

        public MyWalletLogViewHolder(View itemView) {
            super(itemView);
            t1 = (TextView) itemView.findViewById(R.id.tv_place1);
            t2 = (TextView) itemView.findViewById(R.id.tv_place2);
            t3 = (TextView) itemView.findViewById(R.id.tv_place3);

            i1 = (ImageView) itemView.findViewById(R.id.iv_place_1);
            i2 = (ImageView) itemView.findViewById(R.id.iv_place_2);
            i3 = (ImageView) itemView.findViewById(R.id.iv_place_3);

            time = (TextView) itemView.findViewById(R.id.time_me_route);
            hint = (TextView) itemView.findViewById(R.id.hint_text);

        }
    }
}
