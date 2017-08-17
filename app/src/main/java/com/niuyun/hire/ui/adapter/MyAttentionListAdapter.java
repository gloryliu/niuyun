package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.MyAttentionListBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class MyAttentionListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<MyAttentionListBean.DataBeanX.DataBean> list;
    private  Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private RecyclerViewCommonInterface commonInterface;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public MyAttentionListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public MyAttentionListAdapter(Context context, List<MyAttentionListBean.DataBeanX.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<MyAttentionListBean.DataBeanX.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public  List<MyAttentionListBean.DataBeanX.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_index_company_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            ((ImageViewHolder) viewHolder).iv_company.setVisibility(View.GONE);
            ((ImageViewHolder) viewHolder).iv_divider.setVisibility(View.INVISIBLE);
            ((ImageViewHolder) viewHolder).tv_position_name.setText(list.get(position).getJobsName());
            ((ImageViewHolder) viewHolder).tv_position_price.setText(list.get(position).getMinwage() / 1000 + "k-" + list.get(position).getMaxwage() / 1000 + "k");
            ((ImageViewHolder) viewHolder).tv_company_name.setText(list.get(position).getCompanyname());
            ((ImageViewHolder) viewHolder).tv_location.setText(list.get(position).getDistrictCn());
            ((ImageViewHolder) viewHolder).tv_work_age.setText(list.get(position).getExperienceCn());
            ((ImageViewHolder) viewHolder).tv_education.setText(list.get(position).getEducationCn());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_company)
        ImageView iv_company;//公司图标
        @BindView(R.id.iv_divider)
        ImageView iv_divider;//
        @BindView(R.id.tv_position_name)
        TextView tv_position_name;//职位名称
        @BindView(R.id.tv_position_price)
        TextView tv_position_price;//薪资
        @BindView(R.id.tv_company_name)
        TextView tv_company_name;//公司名称
        @BindView(R.id.tv_location)
        TextView tv_location;//公司地址
        @BindView(R.id.tv_work_age)
        TextView tv_work_age;//工作年限
        @BindView(R.id.tv_education)
        TextView tv_education;//学历

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commonInterface != null) {
                        commonInterface.onClick(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

}
