package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.MemberRankBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class IndexLiveListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<MemberRankBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;



    public IndexLiveListItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public IndexLiveListItemAdapter(Context context, List<MemberRankBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<MemberRankBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<MemberRankBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_index_live_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

//            ((ImageViewHolder) viewHolder).tv_rank1_title.setText(list.get(position).getName());
//            ((ImageViewHolder) viewHolder).tv_rank.setText(list.get(position).getMoney()/100.00 + "");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 5 : 5;
    }

    /**
     * @param house
     * @param viewHolder 标签
     */
//    private void bindTags(NewCommonHouseBean.DataBean house, ViewHolder viewHolder) {
//        if (null != house.getTags() && house.getTags().size() > 0) {
//            viewHolder.atTags.removeAllViews();
//            int i = 0;
//            for (NewCommonHouseBean.DataBean.TagsBean tag : house.getTags()) {
//                TextView textView = new TextView(context);
//
//                textView.setText(tag.getName());
//                textView.setPadding(9, 5, 9, 5);
//                viewHolder.colorlist = ToolsUtil.getRandonBackGroundColor(i % 5);
//                i++;
//                textView.setBackgroundResource(viewHolder.colorlist.get(1));
//                textView.setTextColor(context.getResources().getColor(viewHolder.colorlist.get(0)));
//                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
//                lp.setMargins(8, 8, 8, 8);
//                viewHolder.atTags.addView(textView, lp);
//            }
//        }
//    }
    public class ImageViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_rank1_title)
//        TextView tv_rank1_title;
//        @BindView(R.id.tv_rank)
//        TextView tv_rank;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (clickListerner != null) {
//                        clickListerner.onClick(list.get(getAdapterPosition()));
//                    }
                }
            });
        }
    }

}
