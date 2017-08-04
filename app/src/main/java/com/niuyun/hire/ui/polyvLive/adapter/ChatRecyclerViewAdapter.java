package com.niuyun.hire.ui.polyvLive.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.easefun.polyvsdk.rtmp.chat.PolyvChatMessage;
import com.niuyun.hire.R;

import java.util.LinkedList;

public class ChatRecyclerViewAdapter extends AbsRecyclerViewAdapter{
    private LinkedList<PolyvChatMessage> ls_messages;
    private RecyclerView rv_chat;

    public ChatRecyclerViewAdapter(RecyclerView recyclerView, LinkedList<PolyvChatMessage> ls_messages) {
        super(recyclerView);
        this.ls_messages = ls_messages;
        this.rv_chat = recyclerView;
    }

    public void insert(PolyvChatMessage message){
        ls_messages.addFirst(message);
        rv_chat.smoothScrollToPosition(0);
        notifyDataSetChanged();
    }

    @Override
    public ClickableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        bindContext(parent.getContext());
        return new ItemViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.polyv_recyclerview_chat_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ClickableViewHolder holder, int position) {
        if(holder instanceof ItemViewHolder){
            ItemViewHolder viewHolder = (ItemViewHolder)holder;
            PolyvChatMessage chatMessage = ls_messages.get(position);
            String nickName = chatMessage.getUser().getNick()+"  ";
            String message = chatMessage.getValues()[0];
            SpannableStringBuilder span = new SpannableStringBuilder(nickName+message);
            span.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.orange_main)),0,nickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.tv_chat.setText(span);
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return ls_messages.size();
    }

    private class ItemViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder{
        private TextView tv_chat;

        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_chat = $(R.id.tv_chat);
        }
    }
}
