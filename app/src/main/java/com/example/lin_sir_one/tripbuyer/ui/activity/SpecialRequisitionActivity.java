package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by linSir on 16/7/20.特殊要求的界面
 */
public class SpecialRequisitionActivity extends AppCompatActivity {

    @BindView(R.id.rb_special_1) RadioButton r1;
    @BindView(R.id.rb_special_2) RadioButton r2;
    @BindView(R.id.rb_special_3) RadioButton r3;
    @BindView(R.id.rb_special_4) RadioButton r4;
    @BindView(R.id.rb_special_5) RadioButton r5;
    @BindView(R.id.rb_special_6) RadioButton r6;


    @BindView(R.id.ed_special) EditText feedBack;
    @BindView(R.id.iv_back_special) ImageView back;
    @BindView(R.id.tv_count_special) TextView count;
    int SendTime = 0;
    int isHave = 0;
    String area;
    String SFeedBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_requisition);
        ButterKnife.bind(this);
        String special = Shared.getSpecial();

        Log.i("lin", "-----lin----->" + special);

        if (special.split(";").length == 3) {

            switch (special.split(";")[0]) {
                case "1":
                    r1.setChecked(true);
                    SendTime = 1;
                    break;

                case "2":
                    r2.setChecked(true);
                    SendTime = 2;
                    break;

                case "3":
                    r3.setChecked(true);
                    SendTime = 3;
                    break;

                case "4":
                    r4.setChecked(true);
                    SendTime = 4;
                    break;
            }

            switch (special.split(";")[1]) {
                case "1":
                    r5.setChecked(true);
                    isHave = 1;
                    break;

                case "2":
                    r6.setChecked(true);
                    isHave = 2;
                    break;
            }

            feedBack.setText(special.split(";")[2]);

        }


    }

    @OnClick(R.id.rb_special_1)
    public void r1() {
        SendTime = 1;
        Toast.makeText(SpecialRequisitionActivity.this, "okok", Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.rb_special_2)
    public void r2() {
        SendTime = 2;
    }

    @OnClick(R.id.rb_special_3)
    public void r3() {
        SendTime = 3;
    }

    @OnClick(R.id.rb_special_4)
    public void r4() {
        SendTime = 4;
    }

    @OnClick(R.id.rb_special_5)
    public void r5() {
        isHave = 1;
    }

    @OnClick(R.id.rb_special_6)
    public void r6() {
        isHave = 2;
    }


    @OnClick({R.id.iv_back_special, R.id.finish_special_requisition})
    public void back() {

        if (SendTime == 0) {
            Toast.makeText(SpecialRequisitionActivity.this, "发货时间不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isHave == 0) {
            Toast.makeText(SpecialRequisitionActivity.this, "有无包装不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (SFeedBack == null) {
            Toast.makeText(SpecialRequisitionActivity.this, "对买手说的话不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("sendTime", SendTime);
        intent.putExtra("isHave", isHave);
        intent.putExtra("feedBack", SFeedBack);

        String special = SendTime + ";" + isHave + ";" + SFeedBack;
        Shared.saveSpecial(special);

        SpecialRequisitionActivity.this.setResult(RESULT_OK, intent);
        SpecialRequisitionActivity.this.finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("sendTime", SendTime);
        intent.putExtra("isHave", isHave);
        intent.putExtra("feedBack", SFeedBack);

        String special = SendTime + ";" + isHave + ";" + SFeedBack;
        Shared.saveSpecial(special);

        SpecialRequisitionActivity.this.setResult(RESULT_OK, intent);
        SpecialRequisitionActivity.this.finish();
        super.onBackPressed();
    }

    @OnTextChanged(R.id.ed_special)
    public void feedback() {

        SFeedBack = feedBack.getText().toString().trim();

        if (SFeedBack.length() > 100) {
            String Sfeedback_ = SFeedBack.substring(0, 99);
            feedBack.setText(Sfeedback_);
            feedBack.setSelection(99);
        }
        String text = SFeedBack.length() + "/100";
        count.setText(text);
    }
}











