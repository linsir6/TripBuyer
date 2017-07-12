package com.example.lin_sir_one.tripbuyer.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.activity.AllTravelActivity;
import com.example.lin_sir_one.tripbuyer.ui.activity.ReleaseOrderActivity;
import com.example.lin_sir_one.tripbuyer.ui.activity.SellerTravelDetailsActivity;
import com.example.lin_sir_one.tripbuyer.ui.adapter.SellerRecyclerAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lin_sir on 2016/7/7.卖家界面
 */
public class FragmentSeller extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static int CURRENT_PAGE = 1;                            //获取需要请求的页号
    private RecyclerView recyclerView;                              //recyclerView
    private SellerRecyclerAdapter madapter;                          //适配器
    private List<JourneyModel> mlist;       //一个装载数据的集合
    private LinearLayoutManager linearLayoutManager;                //linearLayoutManger
    private HttpResultListener<List<JourneyModel>> listener;     //数据请求的回调接口
    private SwipeRefreshLayout refreshLayout;                       //下拉刷新控件
    private boolean isLoadMore;                                     //是否加载更多

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller, container, false);
        initviews(view);
        initListener();
        refreshData();
        ButterKnife.bind(this, view);
        return view;
    }

    private void initListener() {
        listener = new HttpResultListener<List<JourneyModel>>() {


            @Override
            public void onSuccess(List<JourneyModel> list) {

                KLog.v("-----lin----->" + list.get(0).getMainCity());

                refreshLayout.setRefreshing(false);
                if (isLoadMore) {
                    KLog.i("----0----0>" + isLoadMore);
                    mlist.addAll(list);
                    madapter.refreshList(mlist);
                    madapter.notifyDataSetChanged();
                    madapter.showNoMore();
                } else {
                    KLog.i("----0----1>" + isLoadMore);
                    if (list.size() != 0)
                        mlist = list;
                    madapter.refreshList(mlist);
                    madapter.notifyDataSetChanged();
                }
                KLog.i("----0----3>" + mlist.size());
            }

            @Override
            public void onError(Throwable e) {
                Log.i("lin", "----lin---->   refresh  error  " + e.toString());
                refreshLayout.setRefreshing(false);
                madapter.showNoMore();
            }
        };
    }

    private void initviews(View view) {

        mlist = new ArrayList<>();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_news);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_buyer);

        refreshLayout.setColorSchemeResources(R.color.blue_500, R.color.purple_500, R.color.green_500);
        refreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        madapter = new SellerRecyclerAdapter();
        madapter.addData(mlist);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(madapter);
        recyclerView.addOnScrollListener(new OnRecyclerScrollListener());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), onItemClickListener));
    }

    /**
     * 刷新时,默认请求第一页的数据
     */
    private void refreshData() {
        refreshLayout.setRefreshing(true);
        isLoadMore = false;
        CURRENT_PAGE = 1;
        requestData(1);
    }

    /**
     * 加载更多
     */
    private void loadMoreData() {
        refreshLayout.setRefreshing(false);// 加载更多与刷新不能同时存在
        isLoadMore = true;
        requestData(++CURRENT_PAGE);
    }

    public class OnRecyclerScrollListener extends RecyclerView.OnScrollListener {

        int lastVisibleItem = 0;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (madapter != null && newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == madapter.getItemCount()) {
                loadMoreData();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
        }
    }

    /**
     * 请求数据
     */
    private void requestData(int page) {
        madapter.showLoadMore();
        //ApiService6.getInstance().famous(listener, page);
        A.getA(2, 0, 1).famous2(listener, page);

    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
//            Intent intent = new Intent(getActivity(), SellerTravelDetailsActivity.class);
//            startActivity(intent);

            Intent intent = new Intent();
            intent.putExtra("like", mlist.get(position).getRid());
            intent.setClass(getActivity(), SellerTravelDetailsActivity.class);
            startActivity(intent);

        }
    };


    @OnClick(R.id.release_distance_buyer)
    public void release() {

        if (Shared.getUserInfo() == null) {
            Toast.makeText(getActivity(), "登录后方可发布订单", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getActivity(), ReleaseOrderActivity.class);
        startActivity(intent);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @OnClick(R.id.all_buyer_order)
    public void allAddress() {

        Intent intent = new Intent(getActivity(), AllTravelActivity.class);
        startActivity(intent);

    }


}