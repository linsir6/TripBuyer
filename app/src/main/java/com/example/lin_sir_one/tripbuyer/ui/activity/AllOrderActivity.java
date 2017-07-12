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
import android.widget.EditText;
import android.widget.Spinner;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.FamousPageModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.ui.adapter.AllOrderAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/8/5.所有买家订单界面,
 */
public class AllOrderActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static int CURRENT_PAGE = 1;                            //获取需要请求的页号
    private AllOrderAdapter madapter;                          //适配器
    private List<FamousPageModel> mlist;       //一个装载数据的集合
    private LinearLayoutManager linearLayoutManager;                //linearLayoutManger
    private HttpResultListener<List<FamousPageModel>> listener;     //数据请求的回调接口
    private SwipeRefreshLayout refreshLayout;                       //下拉刷新控件
    private boolean isLoadMore;                                     //是否加载更多
    int flag = 1;   // 1:智能排序  2.价格的升序  3.价格的降序

    @BindView(R.id.product_name_all_order) EditText productName;
    @BindView(R.id.spinner_all_address) Spinner spinner;
    @BindView(R.id.rv_all_order) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_order);
        ButterKnife.bind(this);
        mlist = new ArrayList<>();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 1:
                        flag = 2;
                        isLoadMore = false;
                        A.getA(3, 0, 0).allWanted(listener, 1, "total", "asc");
                        break;
                    case 2:
                        flag = 3;
                        isLoadMore = false;
                        A.getA(3, 0, 0).allWanted(listener, 1, "total", "desc");
                        break;
                    default:
                        flag = 1;
                        isLoadMore = false;
                        A.getA(3, 0, 0).allWanted(listener, 1, "createDate", "asc");
                        break;

                }

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

        madapter = new AllOrderAdapter();
        madapter.addData(mlist);

        listener = new HttpResultListener<List<FamousPageModel>>() {
            @Override
            public void onSuccess(List<FamousPageModel> list) {
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
            Intent intent = new Intent(AllOrderActivity.this, BuyerOrderDetailsActivity.class);
            intent.putExtra("wid", mlist.get(position).getWid());
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
        // 1:智能排序  2.价格的升序  3.价格的降序

        switch (flag) {
            case 1:
                A.getA(3, 0, 0).allWanted(listener, page, "createDate", "asc");
                break;
            case 2:
                A.getA(3, 0, 0).allWanted(listener, page, "total", "asc");
                break;
            case 3:
                A.getA(3, 0, 0).allWanted(listener, page, "total", "desc");
                break;

        }

        A.getA(3, 0, 0).allWanted(listener, page, "createDate", "asc");
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


    /**
     * 搜索
     */
    @OnClick(R.id.search_product)
    public void search() {

        HttpResultListener<List<FamousPageModel>> listener = new HttpResultListener<List<FamousPageModel>>() {
            @Override public void onSuccess(List<FamousPageModel> famousPageModels) {

                madapter.refreshList(famousPageModels);
                Log.i("lin", "-----lin----->   搜索成功");

            }

            @Override public void onError(Throwable e) {
                Log.i("lin", "-----lin----->   搜索失败" + e.toString());
            }
        };


        A.getA(3, 0, 0).q1(listener, "1", productName.getText().toString().trim(), "productName", "asc");

    }


}

