package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by linSir on 16/7/23.代购喜好的界面,
 */
public class HotBoomLikeActivity extends AppCompatActivity {


    @BindView(R.id.ed_hotbomm_ike) EditText feedBack;
    @BindView(R.id.tv_count_hotboomlike) TextView count;

    @BindView(R.id.hotBoom_like_ck_1) CheckBox c1;
    @BindView(R.id.hotBoom_like_ck_2) CheckBox c2;
    @BindView(R.id.hotBoom_like_ck_3) CheckBox c3;
    @BindView(R.id.hotBoom_like_ck_4) CheckBox c4;
    @BindView(R.id.hotBoom_like_ck_5) CheckBox c5;
    @BindView(R.id.hotBoom_like_ck_6) CheckBox c6;
    @BindView(R.id.hotBoom_like_ck_7) CheckBox c7;
    @BindView(R.id.hotBoom_like_ck_8) CheckBox c8;
    @BindView(R.id.hotBoom_like_ck_9) CheckBox c9;
    @BindView(R.id.hotBoom_like_ck_10) CheckBox c10;
    @BindView(R.id.hotBoom_like_ck_11) CheckBox c11;
    @BindView(R.id.hotBoom_like_ck_12) CheckBox c12;
    @BindView(R.id.hotBoom_like_ck_13) CheckBox c13;


    String content1 = "";
    String content2 = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotboomlike);
        ButterKnife.bind(this);

        String shared = Shared.getHotBoomLike();
        String[] shareds = shared.split(";");

        feedBack.setText(shareds[shareds.length - 1]);

        for (int i = 0; i < shareds.length; i++) {
            switch (shareds[i]) {
                case "1":
                    c1.setChecked(true);
                    break;
                case "2":
                    c2.setChecked(true);
                    break;
                case "3":
                    c3.setChecked(true);
                    break;
                case "4":
                    c4.setChecked(true);
                    break;
                case "5":
                    c5.setChecked(true);
                    break;
                case "6":
                    c6.setChecked(true);
                    break;
                case "7":
                    c7.setChecked(true);
                    break;
                case "8":
                    c8.setChecked(true);
                    break;
                case "9":
                    c9.setChecked(true);
                    break;
                case "10":
                    c10.setChecked(true);
                    break;
                case "11":
                    c11.setChecked(true);
                    break;
                case "12":
                    c12.setChecked(true);
                    break;
                default:

                    break;
            }


        }


    }


    @OnClick(R.id.iv_back_special)
    public void back() {
        finish();
    }

    @OnClick(R.id.finish_special_requisition)
    public void ok() {

        if (c1.isChecked())
            content1 = content1 + "美容护肤;";

        if (c2.isChecked())
            content1 = content1 + "母婴产品;";

        if (c3.isChecked())
            content1 = content1 + "数码家电;";

        if (c4.isChecked())
            content1 = content1 + "品牌护肤;";

        if (c5.isChecked())
            content1 = content1 + "图书音像;";

        if (c6.isChecked())
            content1 = content1 + "生活百货;";

        if (c7.isChecked())
            content1 = content1 + "动漫周边;";

        if (c8.isChecked())
            content1 = content1 + "医疗保健;";

        if (c9.isChecked())
            content1 = content1 + "箱包鞋帽;";

        if (c10.isChecked())
            content1 = content1 + "地方特产;";

        if (c11.isChecked())
            content1 = content1 + "零食饮品;";

        if (c12.isChecked())
            content1 = content1 + "工艺产品;";


        KLog.i("-----lin-----> content1" + content1);
        content2 = feedBack.getText().toString();
        KLog.i("-----lin-----> content1" + content2);


        Shared.saveHotBoomLike(content1 + "-" + content2);

        if (content2.equals("")) {
            Toast.makeText(HotBoomLikeActivity.this, "请填写特殊说明", Toast.LENGTH_SHORT).show();
            return;
        }

        if (content1.equals("")) {
            Toast.makeText(HotBoomLikeActivity.this, "请选择代购喜好", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("content1", content1);
        intent.putExtra("content2", content2);

        content1 = "";
        content2 = "";

        HotBoomLikeActivity.this.setResult(RESULT_OK, intent);
        HotBoomLikeActivity.this.finish();


    }

    @OnTextChanged(R.id.ed_hotbomm_ike)
    public void feedback() {
        String SFeedBack = feedBack.getText().toString().trim();
        if (SFeedBack.length() > 100) {
            String Sfeedback_ = SFeedBack.substring(0, 99);
            feedBack.setText(Sfeedback_);
            feedBack.setSelection(99);
        }
        String text = SFeedBack.length() + "/100";
        count.setText(text);
    }

    @OnClick(R.id.hotBoom_like_ck_13)
    public void checkedAll() {

        c1.setChecked(true);
        c2.setChecked(true);
        c3.setChecked(true);
        c4.setChecked(true);
        c5.setChecked(true);
        c6.setChecked(true);
        c7.setChecked(true);
        c8.setChecked(true);
        c9.setChecked(true);
        c10.setChecked(true);
        c11.setChecked(true);
        c12.setChecked(true);


    }

}















