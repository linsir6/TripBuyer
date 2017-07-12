package com.example.lin_sir_one.tripbuyer.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lin_sir_one.tripbuyer.AddFavoriteToWXActivity;
import com.example.lin_sir_one.tripbuyer.Constants;
import com.example.lin_sir_one.tripbuyer.GetFromWXActivity;
import com.example.lin_sir_one.tripbuyer.PayActivity;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.SendToWXActivity;
import com.example.lin_sir_one.tripbuyer.ShowFromWXActivity;
import com.socks.library.KLog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;

    private Button gotoBtn, regBtn, launchBtn, checkBtn, payBtn, favButton;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);

        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);

        regBtn = (Button) findViewById(R.id.reg_btn);
        regBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 将该app注册到微信
                api.registerApp(Constants.APP_ID);
            }
        });

        gotoBtn = (Button) findViewById(R.id.goto_send_btn);
        gotoBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startActivity(new Intent(WXEntryActivity.this, SendToWXActivity.class));
                Intent intent = getIntent();
                String wid = intent.getExtras().getString("wid");
                intent = new Intent(WXEntryActivity.this, SendToWXActivity.class);
                intent.putExtra("wid", wid);
                KLog.i("----linsirdong---->" + wid);
                startActivity(intent);

                finish();
            }
        });

        launchBtn = (Button) findViewById(R.id.launch_wx_btn);
        launchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(WXEntryActivity.this, "launch result = " + api.openWXApp(), Toast.LENGTH_LONG).show();
            }
        });

        checkBtn = (Button) findViewById(R.id.check_timeline_supported_btn);
        checkBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int wxSdkVersion = api.getWXAppSupportAPI();
                if (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION) {
                    Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline supported", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(WXEntryActivity.this, "wxSdkVersion = " + Integer.toHexString(wxSdkVersion) + "\ntimeline not supported", Toast.LENGTH_LONG).show();
                }
            }
        });

        payBtn = (Button) findViewById(R.id.goto_pay_btn);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String wid = intent.getExtras().getString("wid");
                KLog.i("----linsirdong---->" + wid);
                Intent intent2 = new Intent(WXEntryActivity.this, PayActivity.class);
                intent2.putExtra("wid", wid);

                startActivity(intent2);
            }
        });

        favButton = (Button) findViewById(R.id.goto_fav_btn);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WXEntryActivity.this, AddFavoriteToWXActivity.class));
                finish();
            }
        });

        // debug

        // debug end

        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT).show();

        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                Toast.makeText(this, R.string.launch_from_wx, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();

        if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            Toast.makeText(this, "code = " + ((SendAuth.Resp) resp).code, Toast.LENGTH_SHORT).show();
        }

        int result = 0;

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void goToGetMsg() {
        Intent intent = new Intent(this, GetFromWXActivity.class);
        intent.putExtras(getIntent());
        startActivity(intent);
        finish();
    }

    private void goToShowMsg(ShowMessageFromWX.Req showReq) {
        WXMediaMessage wxMsg = showReq.message;
        WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

        StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
        msg.append("description: ");
        msg.append(wxMsg.description);
        msg.append("\n");
        msg.append("extInfo: ");
        msg.append(obj.extInfo);
        msg.append("\n");
        msg.append("filePath: ");
        msg.append(obj.filePath);

        Intent intent = new Intent(this, ShowFromWXActivity.class);
        intent.putExtra(Constants.ShowMsgActivity.STitle, wxMsg.title);
        intent.putExtra(Constants.ShowMsgActivity.SMessage, msg.toString());
        intent.putExtra(Constants.ShowMsgActivity.BAThumbData, wxMsg.thumbData);
        startActivity(intent);
        finish();
    }
}