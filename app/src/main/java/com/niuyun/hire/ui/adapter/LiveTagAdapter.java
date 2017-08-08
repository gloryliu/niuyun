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
public class LiveTagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<CommonTagItemBean> list;
    private static List<CommonTagItemBean> selectedList = new ArrayList<>();
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private int selectedPosition = -1;
    private RecyclerViewCommonInterface commonInterface;

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public LiveTagAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public LiveTagAdapter(Context context, List<CommonTagItemBean> items) {
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

    public void setSelectedList(List<CommonTagItemBean> items) {
        this.selectedList.clear();
        this.selectedList.addAll(items);
        notifyDataSetChanged();
    }

    public static List<CommonTagItemBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_live_info_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            ((ImageViewHolder) viewHolder).tv_title.setText(list.get(position).getCName());
            if (selectedList.size() > 0) {
                for (int i = 0; i < selectedList.size(); i++) {
                    if (list.get(position).getCId() == selectedList.get(i).getCId()) {
                        ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_000000));
                        ((ImageViewHolder) viewHolder).tv_title.setBackground(context.getResources().getDrawable(R.drawable.bg_fff_round_press_box));
                        break;
                    } else {
                        ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                        ((ImageViewHolder) viewHolder).tv_title.setBackground(context.getResources().getDrawable(R.drawable.bg_fff_round_normal_box));
                    }
                }
            } else {
                ((ImageViewHolder) viewHolder).tv_title.setTextColor(context.getResources().getColor(R.color.color_ffffff));
                ((ImageViewHolder) viewHolder).tv_title.setBackground(context.getResources().getDrawable(R.drawable.bg_fff_round_normal_box));
            }


        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;

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
