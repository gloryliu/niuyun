package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.CommonTagItemBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class CommonPerfectInfoTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<CommonTagItemBean> list;
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

    public CommonPerfectInfoTagAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public CommonPerfectInfoTagAdapter(Context context, List<CommonTagItemBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<CommonTagItemBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<CommonTagItemBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_perfect_info_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            if (selectedPosition == position) {
                ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_333333));
            } else {
                ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_999999));
            }
            ((ImageViewHolder) viewHolder).tv_title.setText(list.get(position).getCName());
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
//        @BindView(R.id.tv_rank)
//        TextView tv_rank;

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
