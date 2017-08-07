package com.niuyun.hire.ui.polyvLive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class ResolutionItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<String> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public RecyclerViewCommonInterface getClickListerner() {
        return clickListerner;
    }

    public void setClickListerner(RecyclerViewCommonInterface clickListerner) {
        this.clickListerner = clickListerner;
    }

    private RecyclerViewCommonInterface clickListerner;

    public ResolutionItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ResolutionItemAdapter(Context context, List<String> items) {
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
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.resolution_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

            ((ImageViewHolder) viewHolder).tv_rank.setText(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_rank)
        TextView tv_rank;

        ImageViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListerner != null) {
                        clickListerner.onClick(list.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

}
