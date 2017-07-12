package com.example.lin_sir_one.tripbuyer.leancloudchatkit.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.example.lin_sir_one.tripbuyer.R;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.LCChatKit;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.adapter.LCIMCommonListAdapter;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.cache.LCIMConversationItemCache;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.event.LCIMIMTypeMessageEvent;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.event.LCIMOfflineMessageCountChangeEvent;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.view.LCIMDividerItemDecoration;
import com.example.lin_sir_one.tripbuyer.leancloudchatkit.viewholder.LCIMConversationItemHolder;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;


/**
 * Created by wli on 16/2/29.
 * 会话列表页
 */
public class LCIMConversationListFragment extends Fragment {
    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView recyclerView;

    protected LCIMCommonListAdapter<AVIMConversation> itemAdapter;
    protected LinearLayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lcim_conversation_list_fragment, container, false);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_conversation_srl_pullrefresh);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_conversation_srl_view);

        refreshLayout.setEnabled(false);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new LCIMDividerItemDecoration(getActivity()));
        itemAdapter = new LCIMCommonListAdapter<AVIMConversation>(LCIMConversationItemHolder.class);
        recyclerView.setAdapter(itemAdapter);
        EventBus.getDefault().register(this);
        updateConversationList();




        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateConversationList();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateConversationList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 收到对方消息时响应此事件
     *
     * @param event
     */
    public void onEvent(LCIMIMTypeMessageEvent event) {
        updateConversationList();
    }

    /**
     * 刷新页面
     */
    private void updateConversationList() {
        List<String> convIdList = LCIMConversationItemCache.getInstance().getSortedConversationList();
        List<AVIMConversation> conversationList = new ArrayList<>();
        for (String convId : convIdList) {
            conversationList.add(LCChatKit.getInstance().getClient().getConversation(convId));
        }

        itemAdapter.setDataList(conversationList);
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * 离线消息数量发生变化是响应此事件
     * 避免登陆后先进入此页面，然后才收到离线消息数量的通知导致的页面不刷新的问题
     *
     * @param updateEvent
     */

    public void onEvent(LCIMOfflineMessageCountChangeEvent updateEvent) {
        updateConversationList();
    }
}
