package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.customview.TimePicker.OnWheelScrollListener;
import com.example.lin_sir_one.tripbuyer.customview.TimePicker.WheelView;
import com.example.lin_sir_one.tripbuyer.customview.adapter.NumericWheelAdapter;
import com.example.lin_sir_one.tripbuyer.event.FirstEvent;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.socks.library.KLog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by linSir on 16/7/22.发布行程的界面,
 */
public class ReleaseTravelActivity extends AppCompatActivity {

    @BindView(R.id.tv_travel_date) TextView travelDate;            //出行日期
    @BindView(R.id.back_time_release_distance) TextView backDate;  //返回日期
    @BindView(R.id.travel_from_distance) EditText travelFrom;      //出行地点
    @BindView(R.id.place2) RelativeLayout place2;                  //旅行地点2
    @BindView(R.id.place3) RelativeLayout place3;                  //旅行地点3
    @BindView(R.id.textView28) TextView many;                      //还可以添加几个国家

    @BindView(R.id.travel_where_1) TextView where1;                //旅行国家+主要城市 1
    @BindView(R.id.travel_where_2) TextView where2;                //旅行国家+主要城市 2
    @BindView(R.id.travel_where_3) TextView where3;                //旅行国家+主要城市 3
    @BindView(R.id.traver_from_distance) RelativeLayout rl_fromPlace;
    @BindView(R.id.begin_spinner) Spinner begin_spinner;

    private int count = 2;
    private int trip_or_back = 0;
    private Dialog setPasswordDialog;
    private LayoutInflater inflater = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    boolean isMonthSetted = false, isDaySetted = false;
    private HttpResultListener<Boolean> listener;
    private static final String[] mCountries = {"北京", "上海", "杭州",
            "烟台", "深圳", "济南", "成都", "长春", "天津", "大连", "郑州", "沈阳", "哈尔滨", "厦门"
            , "西安", "青岛", "福州", "南京", "重庆", "乌鲁木齐", "海口", "南昌", "昆明", "长沙", "珠海"
            , "武汉", "宁波", "桂林", "三亚", "广州"};
    LinearLayout ll;
    TextView tv1, tv2;
    View view = null;
    String _perference = "";
    String _instruction = "";
    String _startPlace;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_distance);
        ButterKnife.bind(this);
        add_list();
        EventBus.getDefault().register(this);
        //where1.setText("aaa");
        listener = new HttpResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(ReleaseTravelActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(ReleaseTravelActivity.this, "发送失败" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        begin_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _startPlace = mCountries[i];
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void add_list() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, mCountries);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        begin_spinner.setAdapter(adapter);
    }

    @OnClick(R.id.back_release_distance)
    public void back() {
        finish();
    }

    @OnClick(R.id.travel_dates)
    public void setTravelDate() {           //设置出行日期
        trip_or_back = 1;
        showSetPasswordDialog(this);
    }

    @OnClick(R.id.back_date)
    public void backDate() {
        trip_or_back = 2;
        showSetPasswordDialog(this);
    }

    @OnClick(R.id.travel_place_1)           //设置旅行地点1
    public void travelWhere1() {
        Intent intent = new Intent(ReleaseTravelActivity.this, SelectCountry.class);
        startActivity(intent);


    }

    @OnClick(R.id.travel_place_2)           //设置旅行地点2
    public void travelWhere2() {

    }

    @OnClick(R.id.travel_place_3)           //设置旅行地点3
    public void travelWhere3() {

    }

    @OnClick(R.id.delete_place_1)           //删除旅行地点1
    public void delete_place_1() {
        Toast.makeText(ReleaseTravelActivity.this, "此国家不能删除", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.delete_place_2)           //删除旅行地点2
    public void delete_place_2() {
        place2.setVisibility(View.GONE);
        count++;
        if (count == 2)
            many.setText("还可以添加两个国家");

        if (count == 1)
            many.setText("还可以添加一个国家");

        if (count == 0)
            many.setText("还可以添加0个国家");
    }

    @OnClick(R.id.delete_place_3)           //删除旅行地点3
    public void delete_place_3() {
        place3.setVisibility(View.GONE);
        count++;
        if (count == 2)
            many.setText("还可以添加两个国家");

        if (count == 1)
            many.setText("还可以添加一个国家");

        if (count == 0)
            many.setText("还可以添加0个国家");
    }

    @OnClick(R.id.hotBoom_like)             //设置代购喜好
    public void hotBoom_like() {
        Intent intent = new Intent(ReleaseTravelActivity.this, HotBoomLikeActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.buyer_role_release_distance)
    public void rule() {
        Intent intent = new Intent(ReleaseTravelActivity.this, BuyerRuleActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.add_country)
    public void addCountry() {

        if (place2.getVisibility() != View.VISIBLE && place3.getVisibility() != View.VISIBLE) {
            place2.setVisibility(View.VISIBLE);
            count--;
        } else if (place2.getVisibility() == View.VISIBLE && place3.getVisibility() != View.VISIBLE) {
            place3.setVisibility(View.VISIBLE);
            count--;
        } else if (place3.getVisibility() == View.VISIBLE && place2.getVisibility() != View.VISIBLE) {
            place2.setVisibility(View.VISIBLE);
            count--;
        } else {
            Toast.makeText(ReleaseTravelActivity.this, "最多添加三个国家", Toast.LENGTH_SHORT).show();
        }

        if (count == 2)
            many.setText("还可以添加两个国家");

        if (count == 1)
            many.setText("还可以添加一个国家");

        if (count == 0)
            many.setText("还可以添加0个国家");


    }


    @OnClick(R.id.traver_from_distance)
    public void traver_from_distance() {
        rl_fromPlace.requestFocus();//获得焦点editText.requestFocus();//获得
    }


    private void showSetPasswordDialog(final Context mContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_timepicker, null);

        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        ll = (LinearLayout) mView.findViewById(R.id.ll);
        ll.addView(getDataPick());
        //tv1 = (TextView) mView.findViewById(R.id.tv1);//
        //tv2 = (TextView) mView.findViewById(R.id.tv2);//
        TextView sure = (TextView) mView.findViewById(R.id.sure);
        sure.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setPasswordDialog.dismiss();
            }
        });
        builder.setView(mView);
        builder.setCancelable(true);
        setPasswordDialog = builder.create();
        setPasswordDialog.show();

    }

    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR) + 2;

        int curYear = mYear;
        int curMonth = mMonth + 1;
        int curDate = mDay;

        view = inflater.inflate(R.layout.wheel_date_picker, null);

        year = (WheelView) view.findViewById(R.id.year);

        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 2016, norYear);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(false);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(false);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 1, 31, "%02d");
        numericWheelAdapter3.setLabel("日");
        day.setViewAdapter(numericWheelAdapter3);
        day.addScrollingListener(scrollListener);
        initDay(curYear, curMonth);
        day.setCyclic(false);

        year.setVisibleItems(7);
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);


        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

            int n_year = year.getCurrentItem() + 2016;
            int n_month = month.getCurrentItem() + 1;
            initDay(n_year, n_month);
            int n_day = day.getCurrentItem() + 1;

            String time = n_year + "." + n_month + "." + n_day;

            if (trip_or_back == 1)
                travelDate.setText(time);

            if (trip_or_back == 2)
                backDate.setText(time);

        }
    };


    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     *
     */
    private void initDay(int arg1, int arg2) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @OnClick(R.id.send_travel_release)
    public void release() {


        String _travelDate = travelDate.getText().toString();


        if (_travelDate == null) {
            Toast.makeText(ReleaseTravelActivity.this, "出行日期不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (_perference == null) {
            Toast.makeText(ReleaseTravelActivity.this, "代购喜好不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (_instruction == null) {
            Toast.makeText(ReleaseTravelActivity.this, "特殊说明不能为空", Toast.LENGTH_SHORT).show();
            return;
        }


        _travelDate = _travelDate.replaceAll("\\.", "-");
        Log.i("lin", "----lin---->traver Date : " + _travelDate);

        //String _startPlace = travelFrom.getText().toString();


//        if (_startPlace.equals("")) {
//            Toast.makeText(ReleaseTravelActivity.this, "出行地点不能为空", Toast.LENGTH_SHORT).show();
//            return;
//        }
        String _travelPlace = "";
        String _mainCity = "";

        String _returnDate = "";
        try {
            _travelPlace = where1.getText().toString().split("-")[0];
            _mainCity = where1.getText().toString().split("-")[1];
            _returnDate = backDate.getText().toString();
        } catch (Exception e) {

        }


        _returnDate = _returnDate.replaceAll("\\.", "-");
        Log.i("lin", "----lin---->return Date : " + _returnDate);


        if (_travelPlace.equals("")) {
            Toast.makeText(ReleaseTravelActivity.this, "旅行地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (_mainCity.equals("")) {
            Toast.makeText(ReleaseTravelActivity.this, "旅行地点不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (_returnDate.equals("")) {
            Toast.makeText(ReleaseTravelActivity.this, "返程日期不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        //ApiService6.getInstance().route(listener, _travelDate, _startPlace, _travelPlace, _mainCity, _returnDate, _perference, _instruction);
        //A.getA(2, 0, 1).route(listener, _travelDate, _startPlace, _travelPlace, _mainCity, _returnDate, _perference, _instruction);


        try {
            _perference = _perference.substring(0, _perference.length() - 1);
        } catch (Exception e) {

        }
        KLog.i("-lin--_perference->" + _perference);

        KLog.i("-lin--_instruction->" + _instruction);

        A.getA(2, 0, 1).route(listener, _travelDate, _startPlace, _travelPlace, _mainCity, _returnDate, _perference, _instruction);


    }


    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            _instruction = data.getExtras().getString("content2");
            _perference = data.getExtras().getString("content1");
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(FirstEvent event) {
        KLog.i("____lin____" + event.getMsg());
        String msg = event.getMsg();
        if (msg.split(";")[0].equals("1")) {
            where1.setText(msg.split(";")[1]);
        }
        if (msg.split(";")[0].equals("2")) {
            where2.setText(msg.split(";")[1]);
        }
        if (msg.split(";")[0].equals("3")) {
            where3.setText(msg.split(";")[1]);
        }

    }


}








