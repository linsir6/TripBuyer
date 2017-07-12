package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.utils.MD5;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by linSir on 16/9/19.提现界面
 */
public class WithdrawActivity extends AppCompatActivity {

    @BindView(R.id.zhi_fu_bao) EditText zhiFuBao;
    @BindView(R.id.balance) EditText money;
    @BindView(R.id.input_pwd_withdraw) EditText pwd;
    @BindView(R.id.zhi_fu_bao_name) EditText zhiFuBao_name;
    private Boolean isHasZhiFuBao = false;
    private Boolean isHasPwd = false;
    private Dialog setPayPwdDialog;
    private String _zhiFuBao = "";


    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        ButterKnife.bind(this);
        HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
            @Override public void onSuccess(Obj obj) {
                int ii = obj.getIsHavePassword();
                if (ii == 0) {
                    isHasPwd = false;
                    showSetPayPwdDialog(WithdrawActivity.this);
                } else {
                    isHasPwd = true;
                }

            }

            @Override public void onError(Throwable e) {
                Toast.makeText(WithdrawActivity.this, "提现界面启动失败，请重新尝试", Toast.LENGTH_LONG).show();
            }
        };
        A.getA(4, 0, 1).purse(listener, "1");


    }

    @OnClick(R.id.withdraw)
    public void withdraw() {
        if (!isHasPwd) {
            showSetPayPwdDialog(WithdrawActivity.this);
        } else {

            HttpResultListener<String> listener = new HttpResultListener<String>() {
                @Override public void onSuccess(String s) {
                    Toast.makeText(WithdrawActivity.this, "提现成功", Toast.LENGTH_LONG).show();
                }

                @Override public void onError(Throwable e) {
                    Toast.makeText(WithdrawActivity.this, "提现失败"+e.toString(), Toast.LENGTH_LONG).show();
                }
            };
            A.getA(4, 0, 1).newWithdraw(listener, zhiFuBao_name.getText().toString(), MD5.GetMD5Code(pwd.getText().toString()), money.getText().toString(), zhiFuBao.getText().toString());

        }


    }

    private void showSetPayPwdDialog(Context mContext) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.dialog_set_pay_pwd, null);
        builder.setView(mView);
        builder.setCancelable(true);
        setPayPwdDialog = builder.create();
        setPayPwdDialog.show();

        final EditText e1 = (EditText) mView.findViewById(R.id.e1);
        final EditText e2 = (EditText) mView.findViewById(R.id.e2);
        TextView b1 = (TextView) mView.findViewById(R.id.b1);
        TextView b2 = (TextView) mView.findViewById(R.id.b2);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setPayPwdDialog.dismiss();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View view) {
                HttpResultListener<Obj> listener = new HttpResultListener<Obj>() {
                    @Override public void onSuccess(Obj obj) {
                        Toast.makeText(WithdrawActivity.this, "设置支付密码成功", Toast.LENGTH_SHORT).show();
                        isHasPwd = true;
                        setPayPwdDialog.dismiss();
                    }

                    @Override public void onError(Throwable e) {
                        Toast.makeText(WithdrawActivity.this, "设置支付密码失败", Toast.LENGTH_SHORT).show();

                    }
                };
                if (!e1.getText().toString().equals(e2.getText().toString())) {
                    Toast.makeText(WithdrawActivity.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (e1.getText().toString().length() != 6) {
                    Toast.makeText(WithdrawActivity.this, "密码的长度只能是六尾", Toast.LENGTH_SHORT).show();
                    return;
                }

                A.getA(4, 0, 1).setPayPwd(listener, MD5.GetMD5Code(e1.getText().toString()));
            }
        });
    }
}

