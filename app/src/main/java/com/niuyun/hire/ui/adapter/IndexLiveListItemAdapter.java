package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.LiveListBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.view.AutoNextLineView;
import com.niuyun.hire.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class IndexLiveListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<LiveListBean.DataBeanX.DataBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    private RecyclerViewCommonInterface commonInterface;


    public IndexLiveListItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public IndexLiveListItemAdapter(Context context, List<LiveListBean.DataBeanX.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<LiveListBean.DataBeanX.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<LiveListBean.DataBeanX.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_index_live_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

            ((ImageViewHolder) viewHolder).tv_name.setText(list.get(position).getName());
            ImageLoadedrManager.getInstance().display(context, list.get(position).getCoverImage(), ((ImageViewHolder) viewHolder).iv_live_cover);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    /**
     * @param
     * @param
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
        @BindView(R.id.iv_head)
        CircularImageView iv_head;//头像
        @BindView(R.id.tv_name)
        TextView tv_name;//名称
        @BindView(R.id.tv_company_name)
        TextView tv_company_name;//名称
        @BindView(R.id.tv_look_number)
        TextView tv_look_number;//直播间人数
        @BindView(R.id.iv_is_live)
        ImageView iv_is_live;//是否在直播
        @BindView(R.id.iv_live_cover)
        ImageView iv_live_cover;//封面
        @BindView(R.id.tv_describe)
        TextView tv_describe;//描述
        @BindView(R.id.an_tags)
        AutoNextLineView an_tags;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonInterface != null) {
                        commonInterface.onClick(list.get(getAdapterPosition()));
//                        commonInterface.onClick(0);
                    }
                }
            });
        }
    }

}
