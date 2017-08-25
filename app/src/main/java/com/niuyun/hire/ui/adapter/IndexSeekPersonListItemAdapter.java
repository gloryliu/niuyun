package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.ui.bean.EnterpriseFindPersonBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.ImageLoadedrManager;
import com.niuyun.hire.view.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class IndexSeekPersonListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<EnterpriseFindPersonBean.DataBeanX.DataBean> list;
    private Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private RecyclerViewCommonInterface commonInterface;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public IndexSeekPersonListItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<EnterpriseFindPersonBean.DataBeanX.DataBean> getList() {
        return list;
    }

    public IndexSeekPersonListItemAdapter(Context context, List<EnterpriseFindPersonBean.DataBeanX.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<EnterpriseFindPersonBean.DataBeanX.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<EnterpriseFindPersonBean.DataBeanX.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_index_seek_person_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            try {
                ImageLoadedrManager.getInstance().display(context, Constants.COMMON_PERSON_URL + list.get(position).getAvatars(), ((ImageViewHolder) viewHolder).iv_company, R.mipmap.ic_default_head);

            } catch (Exception e) {
            }
            ((ImageViewHolder) viewHolder).tv_position_name.setText(list.get(position).getFullname());
            ((ImageViewHolder) viewHolder).tv_position_price.setText(list.get(position).getWageCn());
            ((ImageViewHolder) viewHolder).tv_company_name.setText("期望职位:" + list.get(position).getIntentionJobs());
            ((ImageViewHolder) viewHolder).tv_describe.setText(list.get(position).getSpecialty());
            ((ImageViewHolder) viewHolder).tv_location.setText(list.get(position).getExperienceCn());
            ((ImageViewHolder) viewHolder).tv_work_age.setText(" | " + list.get(position).getEducationCn());
            ((ImageViewHolder) viewHolder).tv_education.setText(" | " + list.get(position).getSexCn());
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_company)
        CircularImageView iv_company;//图标
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
        @BindView(R.id.tv_describe)
        TextView tv_describe;//

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
