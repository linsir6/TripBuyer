package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.JourneyModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.ui.adapter.AllTravelAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/7/28.所有行程界面,
 */
public class AllTravelActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static int CURRENT_PAGE = 1;                            //获取需要请求的页号
    private AllTravelAdapter madapter;                          //适配器
    private List<JourneyModel> mlist;       //一个装载数据的集合
    private LinearLayoutManager linearLayoutManager;                //linearLayoutManger
    private HttpResultListener<List<JourneyModel>> listener;     //数据请求的回调接口
    private SwipeRefreshLayout refreshLayout;                       //下拉刷新控件
    private boolean isLoadMore;                                     //是否加载更多

    //@BindView(R.id.select_city_select) EditText country;
    @BindView(R.id.spinner_all_address) Spinner spinner;
    @BindView(R.id.rv_all_address) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_address);
        ButterKnife.bind(this);
        mlist = new ArrayList<>();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("lin", "----lin---->" + i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.rf_all_address);

        refreshLayout.setColorSchemeResources(R.color.blue_500, R.color.purple_500, R.color.green_500);
        refreshLayout.setOnRefreshListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);

        madapter = new AllTravelAdapter();
        madapter.addData(mlist);

        listener = new HttpResultListener<List<JourneyModel>>() {
            @Override
            public void onSuccess(List<JourneyModel> list) {
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
        linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(madapter);
        mRecyclerView.addOnScrollListener(new OnRecyclerScrollListener());
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));

        refreshData();
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Intent intent = new Intent(AllTravelActivity.this, SellerTravelDetailsActivity.class);
            intent.putExtra("like", mlist.get(position).getRid());
            startActivity(intent);
        }
    };


    /**
     * 加载更多
     */
    private void loadMoreData() {
        refreshLayout.setRefreshing(false);// 加载更多与刷新不能同时存在
        isLoadMore = true;
        requestData(++CURRENT_PAGE);
    }

    /**
     * 请求数据
     */
    private void requestData(int page) {
        madapter.showLoadMore();
        //ApiService6.getInstance().famous(listener, page);
        A.getA(2, 0, 1).famous2(listener, page);
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


    @Override
    public void onRefresh() {
        refreshData();
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
     * 返回
     */
    @OnClick(R.id.back_select_city)
    public void back() {
        finish();
    }


}

