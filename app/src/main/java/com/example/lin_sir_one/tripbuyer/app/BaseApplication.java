package com.example.lin_sir_one.tripbuyer.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.example.lin_sir_one.tripbuyer.leanchat.CustomUserProvider;
import com.example.lin_sir_one.tripbuyer.leanchat.MainActivity;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKit;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.handler.LCIMMessageHandler;
import com.example.lin_sir_one.tripbuyer.ui.activity.HomeActivity;

import java.util.List;


/**
 * Created by lin_sir on 2016/6/29.全局的定义
 */
public class BaseApplication extends Application {

    private static BaseApplication mApp;
    private final String APP_ID = "pySLrj5CnOhCxhUE5bK7OBUn-gzGzoHsz";
    private final String APP_KEY = "H2hUpKhbI3MGBpsoXfi8F0LS";


    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;


        AVOSCloud.initialize(this, "gDpARhub8jcfHz7vYKGsDhP9-gzGzoHsz", "gvbvf4DiEbIest3NLWocwT3s");
        //AVOSCloud.initialize(this, "pySLrj5CnOhCxhUE5bK7OBUn-gzGzoHsz", "H2hUpKhbI3MGBpsoXfi8F0LS");
        //启动消息推送服务
        PushService.setDefaultPushCallback(this, MainActivity.class);
        //启动即时通信服务
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new LCIMMessageHandler(this));

        PushService.subscribe(this, "public", HomeActivity.class);
        AVInstallation.getCurrentInstallation().saveInBackground();


        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        AVOSCloud.setDebugLogEnabled(true);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);





    }

    public static BaseApplication get() {
        return mApp;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }
}

