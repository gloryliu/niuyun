package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class GoodsDetailItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<String> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public GoodsDetailItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public GoodsDetailItemAdapter(Context context, List<String> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<String> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<String> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.activity_goods_detail_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            try {
                ImageLoadedrManager.getInstance().display(context, list.get(position), ((ImageViewHolder) viewHolder).iv_image);
            } catch (Exception e) {

            }
            ((ImageViewHolder) viewHolder).tv_title.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        ImageView iv_image;
        @BindView(R.id.tv_title)
        TextView tv_title;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

}
