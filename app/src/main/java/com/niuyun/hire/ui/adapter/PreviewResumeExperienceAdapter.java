package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.PreviewResumeBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class PreviewResumeExperienceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<PreviewResumeBean.DataBean.ResumeWorkBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private int selectedPosition;
    private RecyclerViewCommonInterface commonInterface;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public PreviewResumeExperienceAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public PreviewResumeExperienceAdapter(Context context, List<PreviewResumeBean.DataBean.ResumeWorkBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<PreviewResumeBean.DataBean.ResumeWorkBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<PreviewResumeBean.DataBean.ResumeWorkBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_resume_preview_content, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            ((ImageViewHolder) viewHolder).tv_name.setText(list.get(position).getCompanyname() + "    " + list.get(position).getJobs());
            ((ImageViewHolder) viewHolder).tv_time_duration.setText(list.get(position).getStartyear() + "." + list.get(position).getStartmonth() + "-" + list.get(position).getEndyear() + "." + list.get(position).getEndmonth());
            ((ImageViewHolder) viewHolder).tv_content.setText(list.get(position).getAchievements());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time_duration)
        TextView tv_time_duration;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_content)
        TextView tv_content;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                    if (commonInterface != null) {
                        commonInterface.onClick(list.get(selectedPosition));
                    }
                }
            });
        }
    }

}
