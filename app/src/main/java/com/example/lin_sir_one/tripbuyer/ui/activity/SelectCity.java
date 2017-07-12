package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.event.FirstEvent;
import com.example.lin_sir_one.tripbuyer.ui.adapter.SelectCountryAdapter;
import com.example.lin_sir_one.tripbuyer.ui.listener.RecyclerItemClickListener;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by linSir on 16/8/6.选择城市界面
 */
public class SelectCity extends AppCompatActivity {

    @BindView(R.id.rc_select_city) RecyclerView mRl;
    @BindView(R.id.select_city_select) EditText ed;
    @BindView(R.id.search_city_select) ImageView search;


    private List checkList = new ArrayList();
    private String country;
    private SelectCountryAdapter mAdapter;

    //    "中国", "美国", "日本", "韩国", "泰国", "马来西亚", "俄罗斯"
//            , "德国", "英国", "法国", "意大利", "澳大利亚", "加拿大"
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        country = intent.getExtras().getString("country");

        mAdapter = new SelectCountryAdapter();
        mRl.setLayoutManager(new LinearLayoutManager(this));
        mRl.setAdapter(mAdapter);
        mRl.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));

        if (country.equals("俄罗斯")) {
            checkList.add("莫斯科");
            checkList.add("圣彼得堡");
            checkList.add("新西伯利亚");
        }

        if (country.equals("德国")) {
            checkList.add("柏林");
            checkList.add("慕尼黑");
            checkList.add("法兰克福");
        }

        if (country.equals("法国")) {
            checkList.add("巴黎");
            checkList.add("奥尔良");
            checkList.add("里尔");
        }

        if (country.equals("英国")) {
            checkList.add("伦敦");
            checkList.add("伯明翰");
        }

        if (country.equals("意大利")) {
            checkList.add("罗马");
            checkList.add("米兰");
            checkList.add("威尼斯");
            checkList.add("佛罗伦萨");
        }

        if (country.equals("澳大利亚")) {
            checkList.add("");
            checkList.add("");
            checkList.add("");
            checkList.add("");
        }

        if (country.equals("加拿大")) {
            checkList.add("悉尼");
            checkList.add("墨尔本");
            checkList.add("堪培拉");
            checkList.add("阿德雷城");
        }


        if (country.equals("马来西亚")) {
            checkList.add("吉隆坡");
            checkList.add("马六甲");
        }


        if (country.equals("泰国")) {
            checkList.add("曼谷");
            checkList.add("清迈");
            checkList.add("普吉");
        }

        if (country.equals("日本")) {
            checkList.add("东京");
            checkList.add("大阪");
            checkList.add("横滨");
            checkList.add("名古屋");
            checkList.add("富山");
        }

        if (country.equals("韩国")) {
            checkList.add("首尔");
            checkList.add("釜山");
            checkList.add("汉城");
        }

        if (country.equals("美国")) {
            checkList.add("华盛顿");
            checkList.add("纽约");
            checkList.add("芝加哥");
            checkList.add("洛杉矶");
            checkList.add("费城");
            checkList.add("旧金山");

        }
        if (country.equals("中国")) {

            checkList.add("河北省（冀）");
            checkList.add("山西省（晋）");
            checkList.add("辽宁省（辽）");
            checkList.add("吉林省（吉）");
            checkList.add("黑龙江省（黑）");
            checkList.add("江苏省（苏）");
            checkList.add("浙江省（浙）");
            checkList.add("安徽省（皖)）");
            checkList.add("江西省（赣）");
            checkList.add("山东省（鲁）");
            checkList.add("河南省（豫）");
            checkList.add("湖北省（鄂）");
            checkList.add("湖南省（湘）");
            checkList.add("福建省（闽）");
            checkList.add("广东省（粤）");
            checkList.add("海南省（琼）");

            checkList.add("四川省（川、蜀）");
            checkList.add("贵州省（黔、贵）");
            checkList.add("云南省（滇、云）");
            checkList.add("陕西省（陕、秦）");
            checkList.add("甘肃省（甘、陇）");
            checkList.add("青海省（青）");
            checkList.add("北京市");
            checkList.add("天津市");
            checkList.add("上海市");
            checkList.add("重庆市");
            checkList.add("广西壮族自治区");
            checkList.add("内蒙古自治区");
            checkList.add("西藏自治区 ");
            checkList.add("宁夏回族自治区");
            checkList.add("新疆维吾尔自治区");

        }
        if (checkList.size() != 0) {
            mAdapter.refresh(checkList);
        } else {
            EventBus.getDefault().post(new FirstEvent("1;" + country));
            finish();
        }

    }

    public void onEventMainThread(FirstEvent event) {
        KLog.i("____lin____" + event.getMsg());

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            String city = checkList.get(position).toString();
            EventBus.getDefault().post(new FirstEvent("1;" + country + "-" + city));
            finish();
        }
    };
}
