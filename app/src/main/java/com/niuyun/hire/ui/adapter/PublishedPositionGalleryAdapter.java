package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.EnterprisePublishedPositionBean;
import com.niuyun.hire.ui.bean.PositionIntentBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class PublishedPositionGalleryAdapter extends
        RecyclerView.Adapter<PublishedPositionGalleryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<EnterprisePublishedPositionBean.DataBeanX.DataBean> mDatas;
    private RecyclerViewCommonInterface intentTagClickListerner;
    private Context mContext;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    private int selectedPosition;

    public RecyclerViewCommonInterface getIntentTagClickListerner() {
        return intentTagClickListerner;
    }

    public void setIntentTagClickListerner(RecyclerViewCommonInterface intentTagClickListerner) {
        this.intentTagClickListerner = intentTagClickListerner;
    }
    public void addList(List<EnterprisePublishedPositionBean.DataBeanX.DataBean> items) {
        this.mDatas.addAll(items);
        notifyDataSetChanged();
    }

    public List<EnterprisePublishedPositionBean.DataBeanX.DataBean> getmDatas() {
        return mDatas;
    }

    public void ClearData() {
        mDatas.clear();
        notifyDataSetChanged();
    }
    public PublishedPositionGalleryAdapter(Context context, List<EnterprisePublishedPositionBean.DataBeanX.DataBean> datats) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas=new ArrayList<>();
        this.mDatas = datats;
    }

    public PublishedPositionGalleryAdapter(Context context) {
        this.mContext = context;
        this.mDatas=new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTag;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.fragment_index_company_gallery_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mTag = (TextView) view
                .findViewById(R.id.tv_tag);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mTag.setText(mDatas.get(i).getJobsName());
        if (selectedPosition == i) {
            viewHolder.mTag.setTextColor(mContext.getResources().getColor(R.color.color_ea0000));
            viewHolder.mTag.setBackground(mContext.getResources().getDrawable(R.drawable.bg_ea0000_stroke));
        } else {
            viewHolder.mTag.setTextColor(mContext.getResources().getColor(R.color.color_666666));
            viewHolder.mTag.setBackground(mContext.getResources().getDrawable(R.drawable.bg_00000000_solid));
        }
        viewHolder.mTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (intentTagClickListerner != null) {
                    intentTagClickListerner.onClick(mDatas.get(i));
                    selectedPosition = i;
                    notifyDataSetChanged();
                }
            }
        });
    }

}
