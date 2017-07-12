package com.example.lin_sir_one.tripbuyer.leancloudchatkit.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKit;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.cache.LCIMConversationItemCache;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.utils.LCIMConstants;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.utils.LCIMConversationUtils;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.utils.LCIMLogUtils;

import java.util.Arrays;


/**
 * Created by wli on 16/2/29.
 * 会话详情页
 * 包含会话的创建以及拉取，具体的 UI 细节在 LCIMConversationFragment 中
 */
public class LCIMConversationActivity extends AppCompatActivity {

    protected LCIMConversationFragment conversationFragment;
    private ImageView imageView;
    private Dialog setPasswordDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lcim_conversation_activity);
        imageView = (ImageView) findViewById(R.id.back_message);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                finish();
            }
        });

        conversationFragment = (LCIMConversationFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_chat);
        initByIntent(getIntent());
        showSetPasswordDialog(LCIMConversationActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initByIntent(intent);
    }

    private void initByIntent(Intent intent) {
        if (null == LCChatKit.getInstance().getClient()) {
            showToast("please login first!");
            finish();
            return;
        }

        Bundle extras = intent.getExtras();
        if (null != extras) {
            if (extras.containsKey(LCIMConstants.PEER_ID)) {
                getConversation(extras.getString(LCIMConstants.PEER_ID));
            } else if (extras.containsKey(LCIMConstants.CONVERSATION_ID)) {
                String conversationId = extras.getString(LCIMConstants.CONVERSATION_ID);
                updateConversation(LCChatKit.getInstance().getClient().getConversation(conversationId));
            } else {
                showToast("memberId or conversationId is needed");
                finish();
            }
        }
    }

    /**
     * 设置 actionBar title 以及 up 按钮事件
     *
     * @param title
     */
    protected void initActionBar(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            if (null != title) {
                actionBar.setTitle(title);
            }
            actionBar.setDisplayUseLogoEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            finishActivity(RESULT_OK);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (android.R.id.home == item.getItemId()) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 主动刷新 UI
     *
     * @param conversation
     */
    protected void updateConversation(AVIMConversation conversation) {
        if (null != conversation) {
            conversationFragment.setConversation(conversation);
            LCIMConversationItemCache.getInstance().clearUnread(conversation.getConversationId());
            LCIMConversationUtils.getConversationName(conversation, new AVCallback<String>() {
                @Override
                protected void internalDone0(String s, AVException e) {
                    if (null != e) {
                        LCIMLogUtils.logException(e);
                    } else {
                        initActionBar(s);
                    }
                }
            });
        }
    }

    /**
     * 获取 conversation
     * 为了避免重复的创建，createConversation 参数 isUnique 设为 true·
     */
    protected void getConversation(final String memberId) {
        LCChatKit.getInstance().getClient().createConversation(
                Arrays.asList(memberId), "", null, false, true, new AVIMConversationCreatedCallback() {
                    @Override
                    public void done(AVIMConversation avimConversation, AVIMException e) {
                        if (null != e) {
                            showToast(e.getMessage());
                        } else {
                            updateConversation(avimConversation);
                        }
                    }
                });
    }

    /**
     * 弹出 toast
     *
     * @param content
     */
    private void showToast(String content) {
        Toast.makeText(LCIMConversationActivity.this, content, Toast.LENGTH_SHORT).show();
    }


    private void showSetPasswordDialog(final Context mContext) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_order_rule, null);
        LinearLayout iKonw = (LinearLayout) mView.findViewById(R.id.i_konw);
        iKonw.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                setPasswordDialog.dismiss();
            }
        });
        builder.setView(mView);
        builder.setCancelable(false);
        setPasswordDialog = builder.create();
        setPasswordDialog.show();
    }


}