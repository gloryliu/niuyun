package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.JobTagBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class JobPerfectInfoTagAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<JobTagBean.DataBean> list;
    private  Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private int selectedPosition=-1;
    private RecyclerViewCommonInterface commonInterface;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public JobPerfectInfoTagAdapter1(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public JobPerfectInfoTagAdapter1(Context context, List<JobTagBean.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<JobTagBean.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public  List<JobTagBean.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_perfect_info_tag1, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            if (selectedPosition == position) {
                ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_333333));
                ((ImageViewHolder) viewHolder).iv_angle.setVisibility(View.VISIBLE);
            } else {
                ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_999999));
                ((ImageViewHolder) viewHolder).iv_angle.setVisibility(View.GONE);
            }
            ((ImageViewHolder) viewHolder).tv_title.setText(list.get(position).getCategoryname());
//            ((ImageViewHolder) viewHolder).tv_rank.setText(list.get(position).getMoney()/100.00 + "");
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.iv_angle)
        ImageView iv_angle;

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
