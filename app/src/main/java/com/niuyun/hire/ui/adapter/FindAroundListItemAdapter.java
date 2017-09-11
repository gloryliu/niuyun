package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.Constants;
import com.niuyun.hire.ui.bean.AroundResultBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class FindAroundListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AroundResultBean.DataBean> list;
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

    public FindAroundListItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public List<AroundResultBean.DataBean> getList() {
        return list;
    }

    public FindAroundListItemAdapter(Context context, List<AroundResultBean.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<AroundResultBean.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public List<AroundResultBean.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.activity_find_around_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

//            ImageLoadedrManager.getInstance().display(context, Constants.COMMON_IMAGE_URL + list.get(position).getLogo(), ((ImageViewHolder) viewHolder).iv_company);

//            ((ImageViewHolder) viewHolder).tv_position_price.setText(list.get(position).getMinwage() / 1000 + "k-" + list.get(position).getMaxwage() / 1000 + "k");
            ((ImageViewHolder) viewHolder).tv_company_name.setText(list.get(position).getCompanyname());
//            ((ImageViewHolder) viewHolder).tv_location.setText(list.get(position).getDistrictCn());
//
//
            if (list.get(position).getType() == 2) {
                //人
                try {
                    ImageLoadedrManager.getInstance().displayCycle(context, Constants.COMMON_PERSON_URL + list.get(position).getAvatars(), ((ImageViewHolder) viewHolder).iv_company, R.mipmap.ic_default_head);

                } catch (Exception e) {
                }
                ((ImageViewHolder) viewHolder).tv_position_name.setText(list.get(position).getPersonName() + "|" + list.get(position).getSexCn());
                ((ImageViewHolder) viewHolder).tv_describe.setText(list.get(position).getSpecialty());
                ((ImageViewHolder) viewHolder).tv_position_distance.setText(list.get(position).getDistence()+"");
            } else {
                //公司
                try {
                    ImageLoadedrManager.getInstance().display(context, Constants.COMMON_IMAGE_URL + list.get(position).getLogo(), ((ImageViewHolder) viewHolder).iv_company);

                } catch (Exception e) {
                }
                ((ImageViewHolder) viewHolder).tv_position_name.setText(list.get(position).getCompanyname());
                ((ImageViewHolder) viewHolder).tv_describe.setText("热招职位:" + list.get(position).getJobsName());
                ((ImageViewHolder) viewHolder).tv_position_distance.setText(list.get(position).getDistence()+"");
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_company)
        ImageView iv_company;//公司图标
        @BindView(R.id.tv_position_name)
        TextView tv_position_name;//职位名称
        //        @BindView(R.id.tv_position_price)
//        TextView tv_position_price;//薪资
        @BindView(R.id.tv_company_name)
        TextView tv_company_name;//公司名称
        //        @BindView(R.id.tv_location)
//        TextView tv_location;//公司地址
        @BindView(R.id.tv_position_distance)
        TextView tv_position_distance;
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
