package com.niuyun.hire.ui.chat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.chat.adapters.GroupManageMessageAdapter;
import com.niuyun.hire.ui.chat.model.GroupFuture;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;

import java.util.ArrayList;
import java.util.List;

import presentation.presenter.GroupManagerPresenter;
import presentation.viewfeatures.GroupManageMessageView;

public class GroupManageMessageActivity extends Activity implements GroupManageMessageView {

    private final String TAG = "GroupManageMessageActivity";
    private GroupManagerPresenter presenter;
    private ListView listView;
    private List<GroupFuture> list= new ArrayList<>();
    private GroupManageMessageAdapter adapter;
    private final int PAGE_SIZE = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_manage_message);
        presenter = new GroupManagerPresenter(this);
        presenter.getGroupManageMessage(PAGE_SIZE);
        listView =(ListView) findViewById(R.id.list);
        adapter = new GroupManageMessageAdapter(this, R.layout.item_three_line, list);
        listView.setAdapter(adapter);
    }

    /**
     * 获取群管理最后一条系统消息的回调
     *
     * @param message     最后一条消息
     * @param unreadCount 未读数
     */
    @Override
    public void onGetGroupManageLastMessage(TIMGroupPendencyItem message, long unreadCount) {

    }

    /**
     * 获取群管理系统消息的回调
     *
     * @param message 分页的消息列表
     */
    @Override
    public void onGetGroupManageMessage(List<TIMGroupPendencyItem> message) {
        List<GroupFuture> futures = new ArrayList<>();
        for (TIMGroupPendencyItem item : message){
            futures.add(new GroupFuture(item));
        }
        list.addAll(futures);
        adapter.notifyDataSetChanged();

    }
}
