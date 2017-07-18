package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.listerner.IntentTagClickListerner;

import java.util.List;

/**
 * Created by chen.zhiwei on 2017-7-18.
 */

public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private List<String> mDatas;
    private IntentTagClickListerner intentTagClickListerner;
    private Context mContext;

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    private int selectedPosition;

    public IntentTagClickListerner getIntentTagClickListerner() {
        return intentTagClickListerner;
    }

    public void setIntentTagClickListerner(IntentTagClickListerner intentTagClickListerner) {
        this.intentTagClickListerner = intentTagClickListerner;
    }

    public GalleryAdapter(Context context, List<String> datats) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
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
        viewHolder.mTag.setText(mDatas.get(i));
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
                    intentTagClickListerner.onClickListerrner(mDatas.get(i));
                    selectedPosition = i;
                    notifyDataSetChanged();
                }
            }
        });
    }

}
