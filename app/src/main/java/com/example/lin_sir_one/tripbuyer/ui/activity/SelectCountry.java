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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by linSir on 16/8/6.选择国家的界面
 */
public class SelectCountry extends AppCompatActivity {

    @BindView(R.id.rc_select_city) RecyclerView mRl;
    @BindView(R.id.select_city_select) EditText ed;
    @BindView(R.id.search_city_select) ImageView search;

    private SelectCountryAdapter mAdapter;
    String[] countries = {"中国", "美国", "日本", "韩国", "泰国", "马来西亚", "俄罗斯"
            , "德国", "英国", "法国", "意大利", "澳大利亚", "加拿大"
            , "台湾", "香港", "澳门"
            , "朝鲜", "蒙古", "台湾", "香港", "澳门", "菲律宾", "越南", "柬埔寨", "缅甸", "新加坡"
            , "印度尼西亚", "尼泊尔", "不丹", "孟加拉国", "印度", "巴基斯坦", "斯里兰卡", "马尔代夫"
            , "哈萨克斯坦", "吉尔吉斯斯坦", "塔吉克斯坦", "乌兹别克斯坦", "土库曼斯坦", "阿富汗"
            , "伊拉克", "伊朗", "叙利亚", "约旦", "黎巴嫩", "以色列", "巴勒斯坦", "沙特阿拉伯", "卡塔尔"
            , "科威特", "阿拉伯联合酋长国（阿联酋）", "格鲁吉亚", "亚美尼亚", "塞拜疆"
            , "土耳其", "塞浦路斯", "芬兰", "瑞典", "挪威", "冰岛", "丹麦", "立陶宛", "白俄罗斯"
            , "乌克兰", "波兰", "捷克斯洛伐克", "匈牙利", "奥地利", "瑞士", "爱尔兰", "荷兰", "比利时"
            , "卢森堡", "摩纳哥", "罗马尼亚", "保加利亚"
            , "塞尔维亚", "马其顿", "阿尔巴尼亚", "希腊", "斯洛文尼亚", "克罗地亚", "梵蒂冈", "西班牙"
            , "葡萄牙", "非洲", "埃及", "苏丹", "突尼斯", "阿尔及利亚", "摩洛哥,", "埃塞俄比亚,", "肯尼亚"
            , "坦桑尼亚", "乌干达", "卢旺达", "塞舌尔", "喀麦隆", "塞内加尔", "科特迪瓦", "加纳"
            , "尼日利亚", " 赞比亚", "安哥拉", "津巴布韦", "莫桑比克", "纳米比亚", "南非", "斯威士兰"
            , "马达加斯加", "毛里求斯", "留尼旺（法）", "新西兰", "瓦努阿图", "马绍尔群岛", "帕劳"
            , "斐济群岛", "汤加", "库克群岛（新）", "关岛（美）", "法属波利尼西亚", "北马里亚纳（美）"
            , "墨西哥", "格陵兰（丹）", "尼加拉瓜,", "哥斯达黎加", "巴拿马", "巴哈马", "古巴", "牙买加"
            , "海地", "哥伦比亚", "委内瑞拉", "厄瓜多尔", "秘鲁", "玻利维亚", "巴西", "智利", "阿根廷"
            , "乌拉圭", "巴拉圭"
    };

    List countryChecked = new ArrayList();
    List allCountry = new ArrayList();
    List temp = new ArrayList();

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_country);
        ButterKnife.bind(this);
        mAdapter = new SelectCountryAdapter();

        for (int i = 0; i < countries.length; i++) {
            allCountry.add(countries[i]);
        }

        temp = allCountry;
        mRl.setLayoutManager(new LinearLayoutManager(this));
        mRl.addOnItemTouchListener(new RecyclerItemClickListener(this, onItemClickListener));
        mRl.setAdapter(mAdapter);
        mAdapter.refresh(allCountry);
    }

    @OnClick(R.id.search_city_select)
    public void select() {
        for (int i = 0; i < countries.length; i++) {
            if (countries[i].contains(ed.getText().toString())) {
                countryChecked.add(countries[i]);
            }
        }
        mAdapter.refresh(countryChecked);
        temp = countryChecked;
        countryChecked.clear();
    }

    private RecyclerItemClickListener.OnItemClickListener onItemClickListener = new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {

            String country = temp.get(position).toString();
            EventBus.getDefault().post(new FirstEvent(country));
            Intent intent = new Intent(SelectCountry.this, SelectCity.class);
            intent.putExtra("country", country);
            startActivity(intent);
            finish();
        }
    };

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}







