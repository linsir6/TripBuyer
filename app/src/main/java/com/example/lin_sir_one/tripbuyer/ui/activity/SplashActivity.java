package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.lin_sir_one.tripbuyer.R;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by linSir on 16/9/13.引导页
 */
public class SplashActivity extends AppCompatActivity {
    private ViewPager viewPager;
    Boolean flag = false;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        viewPager = (ViewPager) findViewById(R.id.vp_main);
        viewPager.setCurrentItem(2);


        LayoutInflater mLi = LayoutInflater.from(this);

        View view1 = LayoutInflater.from(this).inflate(R.layout.tab1, null);
        View view2 = LayoutInflater.from(this).inflate(R.layout.tab2, null);
        View view3 = LayoutInflater.from(this).inflate(R.layout.tab3, null);

        button = (ImageView) view3.findViewById(R.id.goto_tripbuyer);
        button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final ArrayList<View> views = new ArrayList<View>();
        views.add(view1);
        views.add(view2);
        views.add(view3);
        PagerAdapter mPagerAdapter = new PagerAdapter() {
            //确定页面个数
            @Override
            public int getCount() {
                return views.size();
            }

            //比较
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            //确定需要删除的页面
            @Override
            public void destroyItem(View container, int position, Object object) {
                ((ViewPager) container).removeView(views.get(position));
            }

            //确定需要加载的页面
            @Override
            public Object instantiateItem(View container, int position) {
                ((ViewPager) container).addView(views.get(position));


                return views.get(position);
            }
        };
        viewPager.setAdapter(mPagerAdapter);
    }
}