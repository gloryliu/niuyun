package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.base.Constants;
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
    private List<LiveListBean.DataBeanX.DataBean> list;
    private Context context;
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

    public List<LiveListBean.DataBeanX.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_index_live_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

            ((ImageViewHolder) viewHolder).tv_name.setText(list.get(position).getPublisher());
            ((ImageViewHolder) viewHolder).tv_company_name.setText(list.get(position).getName());
            ((ImageViewHolder) viewHolder).tv_describe.setText(list.get(position).getLiveDescribe());
            ((ImageViewHolder) viewHolder).tv_look_number.setText(list.get(position).getLiveCount() + " 在看");
            if (!TextUtils.isEmpty(list.get(position).getLiveStatus()) && list.get(position).getLiveStatus().contains("直播")) {
                ((ImageViewHolder) viewHolder).iv_is_live.setVisibility(View.VISIBLE);
            } else {
                ((ImageViewHolder) viewHolder).iv_is_live.setVisibility(View.GONE);
            }
            ImageLoadedrManager.getInstance().display(context, Constants.COMMON_cover_URL + list.get(position).getCoverImage(), ((ImageViewHolder) viewHolder).iv_live_cover);
            ImageLoadedrManager.getInstance().display(context, Constants.COMMON_PERSON_URL + list.get(position).getLogoImage(), ((ImageViewHolder) viewHolder).iv_head,R.mipmap.ic_default_head);
            if (!TextUtils.isEmpty(list.get(position).getTagCn())) {
                String arr[] = list.get(position).getTagCn().split(",");
                bindTags(arr, (ImageViewHolder) viewHolder);
            }
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
    private void bindTags(String stringse[], ImageViewHolder viewHolder) {
        if (null != stringse && stringse.length > 0) {
            viewHolder.an_tags.removeAllViews();
            for (String tag : stringse) {
                TextView textView = new TextView(context);

                textView.setText(tag);
                textView.setPadding(9, 5, 9, 5);
                textView.setBackgroundResource(R.drawable.bg_333333_corner);
                textView.setTextColor(context.getResources().getColor(R.color.color_333333));
                ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 10, 10, 10);
                viewHolder.an_tags.addView(textView, lp);
            }
        }
    }

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
