package com.niuyun.hire.ui.chat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.chat.model.Conversation;
import com.niuyun.hire.ui.chat.utils.TimeUtil;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.ui.CircleImageView;

import java.util.List;

/**
 * 会话界面adapter
 */
public class ConversationAdapter extends ArrayAdapter<Conversation> {

    private int resourceId;
    private View view;
    private ViewHolder viewHolder;
    private Context context;

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects  The objects to represent in the ListView.
     */
    public ConversationAdapter(Context context, int resource, List<Conversation> objects) {
        super(context, resource, objects);
        this.context = context;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) view.findViewById(R.id.name);
            viewHolder.avatar = (CircleImageView) view.findViewById(R.id.avatar);
            viewHolder.lastMessage = (TextView) view.findViewById(R.id.last_message);
            viewHolder.time = (TextView) view.findViewById(R.id.message_time);
            viewHolder.unread = (TextView) view.findViewById(R.id.unread_num);
            view.setTag(viewHolder);
        }
        final Conversation data = getItem(position);
        viewHolder.tvName.setText(data.getName());
//        viewHolder.avatar.setImageResource(data.getAvatar());

        viewHolder.avatar.setTag(R.id.glide_tag_id, position);
        if (data.getType() == TIMConversationType.C2C) {
            ImageLoadedrManager.getInstance().display(context, data.getAvaterLive(), viewHolder.avatar, data.getAvatar());
        }else {
            viewHolder.avatar.setImageResource(data.getAvatar());
        }



//        viewHolder.avatar.setTag(R.id.glide_tag_id, position);
//        //待获取用户资料的用户列表
//        if (data.getType() == TIMConversationType.C2C) {
//            List<String> users = new ArrayList<>();
//            users.add(data.getIdentify());
//
////获取用户资料
//            TIMFriendshipManager.getInstance().getUsersProfile(users, new TIMValueCallBack<List<TIMUserProfile>>() {
//                @Override
//                public void onError(int code, String desc) {
//                    //错误码code和错误描述desc，可用于定位请求失败原因
//                    //错误码code列表请参见错误码表
//                    Log.e("", "getUsersProfile failed: " + code + " desc");
//                    viewHolder.avatar.setImageResource(data.getAvatar());
//                }
//
//                @Override
//                public void onSuccess(List<TIMUserProfile> result) {
//                    Log.e("", "getUsersProfile succ");
//                    for (TIMUserProfile res : result) {
//                        Log.e("", "identifier: " + res.getIdentifier() + " nickName: " + res.getNickName()
//                                + " remark: " + res.getRemark());
//                        if (context != null && result.size() > 0) {
//                            ImageLoadedrManager.getInstance().display(context, result.get(0).getFaceUrl(), viewHolder.avatar, data.getAvatar());
////                            Glide.with(context).load(result.get(0).getFaceUrl()).placeholder(data.getAvatar()).into(viewHolder.avatar);
//                        }
//                    }
//                }
//            });
//        }else {
//            viewHolder.avatar.setImageResource(data.getAvatar());
//        }


        viewHolder.lastMessage.setText(data.getLastMessageSummary());
        viewHolder.time.setText(TimeUtil.getTimeStr(data.getLastMessageTime()));
        long unRead = data.getUnreadNum();
        if (unRead <= 0) {
            viewHolder.unread.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.unread.setVisibility(View.VISIBLE);
            String unReadStr = String.valueOf(unRead);
            if (unRead < 10) {
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point1));
            } else {
                viewHolder.unread.setBackground(getContext().getResources().getDrawable(R.drawable.point2));
                if (unRead > 99) {
                    unReadStr = getContext().getResources().getString(R.string.time_more);
                }
            }
            viewHolder.unread.setText(unReadStr);
        }
        return view;
    }

    public class ViewHolder {
        public TextView tvName;
        public CircleImageView avatar;
        public TextView lastMessage;
        public TextView time;
        public TextView unread;

    }
}
