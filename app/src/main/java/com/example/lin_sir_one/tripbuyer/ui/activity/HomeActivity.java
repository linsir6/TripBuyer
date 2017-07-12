package com.example.lin_sir_one.tripbuyer.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.event.FirstEvent;
import com.example.lin_sir_one.tripbuyer.leanchat.MainActivity;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKit;
import com.example.lin_sir_one.tripbuyer.model.Obj;
import com.example.lin_sir_one.tripbuyer.model.UserInfoModel;
import com.example.lin_sir_one.tripbuyer.network.A;
import com.example.lin_sir_one.tripbuyer.network.HttpResultListener;
import com.example.lin_sir_one.tripbuyer.persistence.Shared;
import com.example.lin_sir_one.tripbuyer.ui.adapter.HomeFragmentAdapter;
import com.example.lin_sir_one.tripbuyer.utils.FileUtil;
import com.example.lin_sir_one.tripbuyer.utils.ImageUtil;
import com.example.lin_sir_one.tripbuyer.utils.MD5;
import com.socks.library.KLog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by lin_sir on 2016/7/1.旅行买手主界面,
 */
public class HomeActivity extends AppCompatActivity {


    /**
     * HomeActivity里面的元素,包括侧滑中的元素
     */
    @BindView(R.id.draw_layout)
    DrawerLayout draw_layout;               //drawLayout
    @BindView(R.id.tabLayout_home_activity)
    TabLayout tablayout;        //tabLayout
    @BindView(R.id.iv_user_info_home_activity)
    ImageView userInfo;      //用户头像
    @BindView(R.id.user_name_home_activity)
    TextView username;          //用户姓名
    @BindView(R.id.pager_school)
    ViewPager viewPager;                   //viewPager

    private HomeFragmentAdapter mAdapter;    //适配器
    private Long exitTime = (long) 0;
    Message m = null;
    String password = "";

    /**
     * 输入手机号的对话框
     */
    private Dialog inputTelDialog;          //输入手机号的对话框
    private EditText get_pwd;               //获取输入的手机号
    private ImageView iv_hint;              //输入密码下面的那条线
    private EditText get_tel_login;         //获得到输入的手机号
    private ImageView dismiss_login;        //关闭这个对话框
    private TextView next_login;            //下一步按钮
    private TextView tv_error_login;        //显示输入的手机号有误
    private String _tel;                    //用户手机号
    private String _pwd;                    //用户手机号
    private int chose;                      //选择登录的方式

    /**
     * 获取验证码对话框
     */
    private Dialog getCodeDialog;               //获取验证码的对话框
    private TextView tvCount;                   //倒计时的显示
    private TimeCount timeCount;                //倒计时的实现
    private EditText e1;                        //验证码第一位
    private EditText e2;                        //验证码第二位
    private EditText e3;                        //验证码第三位
    private EditText e4;                        //验证码第四位
    private EditText e5;                        //验证码第五位
    private EditText e6;                        //验证码第六位
    private ImageView back_getCode_dialog;      //返回按钮
    private ImageView dismiss_getCode_dialog;   //关闭按钮
    private TextView showTel;                   //显示手机号
    private String tel_all;                     //六位的验证码

    /**
     * 设置密码对话框
     */
    private Dialog setPasswordDialog;           //设置密码的对话框
    private EditText get_password;              //获取设置完的密码
    private Button finish;                      //结束整个注册逻辑
    private ImageView dissmissSetPwd;           //关闭设置密码的界面


    /**
     * 所有网络请求的回调接口
     */
    private HttpResultListener<Boolean> listener_getCode;       //获取验证码之后的回调接口
    private HttpResultListener<Boolean> listener_login;             //登录之后的回调接口；
    private HttpResultListener<Boolean> listener_register;              //注册之后回调的接口；

    private static final String IMAGE_NAME = "user_avatar.jpg";
    private static final int GALLERY_REQUEST = 102;
    private static final int GALLERY_KITKAT_REQUEST = 103;
    private static final int CAMERA_REQUEST = 104;
    private static final int RESULT = 105;
    private static final int RESULT_CANCELED = 0;
    private Bitmap avatarBmp;


    private File avatarFile;//存放拍照选择的头像文件
    private AVFile avatar;
    private String avatar_file_name = "user_avatar.jpg";

    private AVUser currentUser;
    private ProgressDialog pdWait;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initListeners();
        ButterKnife.bind(this);
        initviews();
        initUser();
        EventBus.getDefault().register(this);
        if (Shared.getUserInfo() != null)
            messageLogin();

        //Shared.saveCookie("JSESSIONID=BC167CAE0D37C231FD75CB3CC030E6FF");
        Log.i("lin", "----lin----> + getCookie " + Shared.getCookie());
        //KLog.i("----ooo----> uid"+ Shared.getUserInfo().getUid());

    }


    private void initListeners() {

        /**
         ＊输入手机号，这个时候会进行判断，如果这个手机号没在手机中出现过，就会收到验证码
         ＊如果这个手机好在手机中出现过，就会弹出输入密码的框框
         */
        listener_getCode = new HttpResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(HomeActivity.this, "验证码已发送到您的手机请注意查收", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HomeActivity.this, "验证码获取失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.i("lin", "----lin----> + listener_getCode " + e.toString());
                Toast.makeText(HomeActivity.this, "发生错误 " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        /**
         * 用于登录之后，的一个事件的监听者，登录可能有两种情况，一种是用账号，密码登陆；另一种使用帐号，验证码登录
         */
        listener_login = new HttpResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
                Toast.makeText(HomeActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                if (getCodeDialog != null) {
                    getCodeDialog.dismiss();
                }
                if (inputTelDialog != null) {
                    inputTelDialog.dismiss();
                }
                UserInfoModel user = Shared.getUserInfo();
                Log.i("lin", "----lin----> + listener_login   user ");

                Log.i("lin", "=====lin=====>   保存的用户姓名 2222" + user.getName());

                Shared.saveTel(user.getName());
                username.setText(user.getName());
//                Picasso.with(HomeActivity.this).load(user.getIv_user()).into(userInfo);
                ImageUtil.requestCircleImg(HomeActivity.this, user.getHead(), userInfo);
                Shared.saveTel(_tel);
                Log.i("lin", "----lin----> + listener_login   登录成功");

                messageLogin();
            }

            @Override
            public void onError(Throwable e) {
                if (e.toString().equals("com.example.lin_sir_one.tripbuyer.network.NetworkException: 用户不存在")) {
                    showSetPasswordDialog(HomeActivity.this);
                } else {
                    Toast.makeText(HomeActivity.this, "验证码错误" + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.i("lin", "-----lin----->" + e.toString());


                }


                Log.i("lin", "----lin----> + listener_login   失败 " + e.toString());

            }
        };


        /**
         * 注册之后的回调接口
         */
        listener_register = new HttpResultListener<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {

                username.setText(_tel);
                Shared.saveTel(_tel);


                Toast.makeText(HomeActivity.this, "设置密码成功", Toast.LENGTH_SHORT).show();
                inputTelDialog.dismiss();
                getCodeDialog.dismiss();
                setPasswordDialog.dismiss();
                A.getA(1, 1, 0).login_usepwd(listener_login, _tel, MD5.GetMD5Code(password));


                messageLogin();
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(HomeActivity.this, "发生错误 ： " + e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("lin", "----lin----> + listener_register   失败 " + e.toString());
            }
        };

    }

    private void initUser() {

        if (Shared.getUserInfo() != null) {
            UserInfoModel user = Shared.getUserInfo();
            Log.i("lin", "----lin----> + initUser " + user.getName());
            username.setText(user.getName());
            ImageUtil.requestCircleImg(HomeActivity.this, user.getHead(), userInfo);

        }

    }

    private void initviews() {
        /**
         * HomeActivity里面的元素,绑定id
         */
        //draw_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(3);
        tablayout.setupWithViewPager(viewPager);
        tablayout.setSelectedTabIndicatorColor(0xffffc107);
        mAdapter.setupTabLayout(tablayout, viewPager);


        for (int i = 0; i < tablayout.getTabCount(); i++) {
            TabLayout.Tab tab = tablayout.getTabAt(i);

            if (tab != null) {
                if (i == 0) {
                    tab.setCustomView(mAdapter.getTabView(i, this, true));
                } else {
                    tab.setCustomView(mAdapter.getTabView(i, this, false));
                }
            }
        }

    }

    /**
     * 打开侧拉栏
     */
    @OnClick(R.id.iv_left_menu_switch)
    public void leftMenu() {
        draw_layout.openDrawer(Gravity.LEFT);
    }

    /**
     * 点击用户头像
     */
    @OnClick({R.id.iv_user_info_home_activity, R.id.user_name_home_activity})
    public void userInfo() {

        if (Shared.getUserInfo() != null) {
            chooseImgDialog();
        } else {
            showInputTelDialog(this);

        }
    }

    @OnClick(R.id.set_me)
    public void set() {
        Intent intent = new Intent(HomeActivity.this, SetActivity.class);
        startActivity(intent);
    }

    /**
     * 旅游行程
     */
    @OnClick(R.id.travel_route_me)
    public void route() {
        Intent intent = new Intent(HomeActivity.this, MeRouteActivity.class);
        startActivity(intent);
    }

    /**
     * 我的订单
     */
    @OnClick(R.id.order_me)
    public void order() {
        Intent intent = new Intent(HomeActivity.this, MeOrderActivity.class);
        startActivity(intent);
    }

    /**
     * 我的钱包
     */
    @OnClick(R.id.wallet_me)
    public void wallet() {
        Intent intent = new Intent(HomeActivity.this, MeWalletActivity.class);
        startActivity(intent);
    }


    private void showInputTelDialog(final Context mContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_no_login, null);

        get_pwd = (EditText) mView.findViewById(R.id.ed_pwd_login_item);
        iv_hint = (ImageView) mView.findViewById(R.id.iv_hint_login_item);
        dismiss_login = (ImageView) mView.findViewById(R.id.dismiss_login_item);
        next_login = (TextView) mView.findViewById(R.id.bt_next_login_item);
        get_tel_login = (EditText) mView.findViewById(R.id.ed_tel_login_item);
        tv_error_login = (TextView) mView.findViewById(R.id.has_error_login);

        builder.setView(mView);
        builder.setCancelable(false);
        inputTelDialog = builder.create();
        inputTelDialog.show();

        get_tel_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                _tel = get_tel_login.getText().toString().trim();
                if (_tel.length() == 11) {
                    next_login.setClickable(true);
                    Resources resources = getBaseContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.select_next);
                    next_login.setBackgroundDrawable(drawable);

                    if (Shared.getTel().equals(_tel)) {
                        chose = 1;
                        iv_hint.setVisibility(View.VISIBLE);
                        get_pwd.setVisibility(View.VISIBLE);
                    } else {
                        chose = 0;
                    }

                } else {
                    next_login.setClickable(false);
                    Resources resources = getBaseContext().getResources();
                    Drawable drawable = resources.getDrawable(R.drawable.button2x);
                    next_login.setBackgroundDrawable(drawable);
                }

                if (_tel.length() > 11) {
                    String _tel11 = _tel.substring(0, 11);
                    get_tel_login.setText(_tel11);
                    get_tel_login.setSelection(11);
                }

                if (_tel.length() >= 1) {
                    String tel_1 = _tel.substring(0, 1);
                    if (!tel_1.equals("1")) {
                        tv_error_login.setVisibility(View.VISIBLE);
                    } else {
                        tv_error_login.setVisibility(View.INVISIBLE);
                    }
                }

                if (_tel.length() == 0) {
                    tv_error_login.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dismiss_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTelDialog.dismiss();
            }
        });

        next_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tel = get_tel_login.getText().toString().trim();
                String pwd = get_pwd.getText().toString().trim();

                if (chose == 0) {
                    showGetCodeDialog(mContext);
                    inputTelDialog.dismiss();

                    A.getA(1, 0, 0).getCode(listener_getCode, _tel);

                } else {
                    A.getA(1, 1, 0).login_usepwd(listener_login, _tel, MD5.GetMD5Code(pwd));
                }
            }
        });


        next_login.setClickable(false);

    }

    private void showGetCodeDialog(Context mContext) {

        final AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_input_code, null);

        dismiss_getCode_dialog = (ImageView) mView.findViewById(R.id.dismiss_input_code);
        back_getCode_dialog = (ImageView) mView.findViewById(R.id.back_input_code);
        showTel = (TextView) mView.findViewById(R.id.tel_input_code);
        tvCount = (TextView) mView.findViewById(R.id.get_code_again);
        e1 = (EditText) mView.findViewById(R.id.getCode_1);
        e2 = (EditText) mView.findViewById(R.id.getCode_2);
        e3 = (EditText) mView.findViewById(R.id.getCode_3);
        e4 = (EditText) mView.findViewById(R.id.getCode_4);
        e5 = (EditText) mView.findViewById(R.id.getCode_5);
        e6 = (EditText) mView.findViewById(R.id.getCode_6);

        timeCount = new TimeCount(60000, 1000);
        showTel.setText(_tel);
        timeCount.start();

        builder2.setView(mView);
        builder2.setCancelable(false);
        getCodeDialog = builder2.create();
        getCodeDialog.show();

        tvCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                A.getA(1, 0, 0).getCode(listener_getCode, _tel);
                timeCount.start();
            }
        });

        e1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e1.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e1.setText(_text);
                    e1.setSelection(1);
                }
                if (text.length() == 1) {
                    e2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        e2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e2.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e2.setText(_text);
                    e2.setSelection(1);
                }
                if (text.length() == 1) {
                    e3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        e3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e3.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e3.setText(_text);
                    e3.setSelection(1);
                }
                if (text.length() == 1) {
                    e4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        e4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e4.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e4.setText(_text);
                    e4.setSelection(1);
                }
                if (text.length() == 1) {
                    e5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        e5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e5.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e5.setText(_text);
                    e5.setSelection(1);
                }
                if (text.length() == 1) {
                    e6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        e6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = e6.getText().toString().trim();
                if (text.length() > 1) {
                    String _text = text.substring(0, 1);
                    e6.setText(_text);
                    e6.setSelection(1);
                }

                if (text.length() == 1) {
                    String e1_text = e1.getText().toString().trim();
                    String e2_text = e2.getText().toString().trim();
                    String e3_text = e3.getText().toString().trim();
                    String e4_text = e4.getText().toString().trim();
                    String e5_text = e5.getText().toString().trim();
                    String e6_text = e6.getText().toString().trim();
                    tel_all = e1_text + e2_text + e3_text + e4_text + e5_text + e6_text;

                    //
                    // ApiService3.getInstance().login_usecode(listener_login, _tel, tel_all);
                    A.getA(1, 1, 0).login_usecode(listener_login, _tel, tel_all);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dismiss_getCode_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTelDialog.dismiss();
                getCodeDialog.dismiss();
            }
        });

        back_getCode_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodeDialog.dismiss();
                inputTelDialog.show();
            }
        });

    }

    private void showSetPasswordDialog(final Context mContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_input_password, null);

        dissmissSetPwd = (ImageView) mView.findViewById(R.id.dismiss_set_password_item);
        get_password = (EditText) mView.findViewById(R.id.get_pwd_password_item);
        finish = (Button) mView.findViewById(R.id.finish_set_password_item);
        builder.setView(mView);
        builder.setCancelable(false);
        setPasswordDialog = builder.create();
        setPasswordDialog.show();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String get_pwd = get_password.getText().toString().trim();

                //ApiService3.getInstance().register(listener_register, _tel, MD5.GetMD5Code(get_pwd), tel_all);
                password = get_pwd;
                A.getA(1, 1, 0).register(listener_register, _tel, MD5.GetMD5Code(get_pwd), tel_all);

            }
        });

        dissmissSetPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTelDialog.dismiss();
                getCodeDialog.dismiss();
                setPasswordDialog.dismiss();
            }
        });
    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            tvCount.setText("获取验证码");
            tvCount.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            tvCount.setClickable(false);//防止重复点击
            tvCount.setText(String.valueOf(millisUntilFinished / 1000) + "秒后重发");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("lin", "----lin----> homeActivity onResume");
        try {

            if (Shared.getCookie() != null) {
                Log.i("lin", "----lin----> homeActivity onResume  " + Shared.getCookie());
                username.setText(Shared.getUserInfo().getName());
                ImageUtil.requestCircleImg(HomeActivity.this, Shared.getUserInfo().getHead(), userInfo);
            } else {
                username.setText("请登录");
                userInfo.setImageResource(R.drawable.chengxin);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000)  //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.iv_message_home_activity)
    public void mseeage() {
        if (Shared.getUserInfo() == null) {
            Toast.makeText(HomeActivity.this, "登录后才能查看我的消息", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void messageLogin() {

        try {
            LCChatKit.getInstance().open(Shared.getUserInfo().getUid(), new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (null == e)
                        Toast.makeText(HomeActivity.this, "聊天界面，登陆成功", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {

        }


    }


    @OnClick(R.id.call_customer_service)
    public void callCustomerService() {
//        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 123456));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        dialog1(builder);
    }

    @OnClick(R.id.reward_me)
    public void reward() {
        Intent intent = new Intent(HomeActivity.this, MeReward.class);
        startActivity(intent);
    }

    protected void dialog1(AlertDialog.Builder builder) {
        builder.setTitle("提示：");
        builder.setPositiveButton("确定", null);
        builder.setMessage("请联系微信客服：123456");
        builder.show();
    }

    /**
     * 选择头像图片的对话框
     */
    private void chooseImgDialog() {
        new android.app.AlertDialog.Builder(HomeActivity.this)
                .setTitle(getResources().getString(R.string.text_choose_image))
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.text_img_album), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromGallery();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.text_img_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fromCamera();
                    }
                }).show();
    }

    /**
     * 启动手机相册
     */
    private void fromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            startActivityForResult(intent, GALLERY_KITKAT_REQUEST);
        } else {
            startActivityForResult(intent, GALLERY_REQUEST);
        }

//        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intent, 103);103
    }

    /**
     * 启动手机相机拍摄照片作为头像
     */
    private void fromCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (FileUtil.hasSdcard()) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));
            startActivityForResult(intent, CAMERA_REQUEST);
        } else {
            Log.i("sys", "--lin--> SD not exist");
        }
    }

    /**
     * 返回结果处理，这里需要注意resultCode，正常情况返回值为 -1 没有任何操作直接后退则返回 0
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 用户进行了有效的操作，结果码不等于取消码的时候
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case GALLERY_REQUEST:                       //从相册选择
                    cropPhoto(data.getData());
                    break;
                case GALLERY_KITKAT_REQUEST:                //从相册选择,兼容版本
                    cropPhoto(data.getData());
                    break;
                case CAMERA_REQUEST://从拍照选择
                    if (FileUtil.hasSdcard()) {
                        cropPhoto(Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMAGE_NAME)));

                    } else {
                        Log.i("sys", "--tc-->EditUserInfo no sd card found");
                    }
                    break;
                case RESULT://选择完成，将头像放在ImageView中
                    if (data != null) {
                        avatarBmp = data.getExtras().getParcelable("data");
                        userInfo.setImageBitmap(ImageUtil.toRoundBitmap(avatarBmp));
                        updateUserInfo(avatarBmp);
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 剪裁照片
     */
    public void cropPhoto(Uri uri) {
        if (uri == null) {
            Log.i("sys", "--tc--> The uri is not exist.");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(HomeActivity.this, uri);
            if (url != null) {
                intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                Log.i("sys", "--tc-->EditUserInfo cropPhoto url is null");
            }
        } else {
            intent.setDataAndType(uri, "image/*");
        }

        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 210);
        intent.putExtra("aspectY", 130);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 130);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT);
    }

    //以下是关键，原本uri返回的是file:///...来着的，android4.4返回的是content:///...
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void updateUserInfo(Bitmap avatar) {

        //如果头像为空，也就是用户没有上传头像，则使用之前的头像地址
        if (avatar == null) {

        } else {
            final AVFile avatarFile = new AVFile("user_avatar.jpeg", ImageUtil.bitmap2Bytes(avatar));
            avatarFile.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {

                        Log.i("lin", "----lin---->  imgUrl" + avatarFile.getUrl());

                        HttpResultListener<Boolean> listener = new HttpResultListener<Boolean>() {
                            @Override public void onSuccess(Boolean aBoolean) {
                                Toast.makeText(HomeActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                                UserInfoModel user = Shared.getUserInfo();
                                Obj obj = new Obj();
                                obj.setName(user.getName());
                                obj.setUid(user.getUid());
                                obj.setIsReal(user.getIsReal());
                                obj.setHead(avatarFile.getUrl());
                                Shared.saveUserInfo(obj);
                                ImageUtil.requestCircleImg(HomeActivity.this, avatarFile.getUrl(), userInfo);
                            }

                            @Override public void onError(Throwable e) {
                                Toast.makeText(HomeActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                            }
                        };

                        A.getA(1, 0, 1).updateHead(listener, avatarFile.getUrl());


                    }
                }
            });
        }
    }

    public void onEvent(FirstEvent event) {
        if (event.getMsg().equals("exit")) {
            KLog.i("----0000----> exit");
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }
}























