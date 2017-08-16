package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.SearchHistoryBean;
import com.niuyun.hire.ui.listerner.RecyclerViewCommonInterface;
import com.niuyun.hire.ui.utils.service.SearchHistoryService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  List<SearchHistoryBean> list;
    private  List<SearchHistoryBean> selectedList = new ArrayList<>();
    private  Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;
    private int selectedPosition = -1;
    private RecyclerViewCommonInterface commonInterface;
    private SearchHistoryService searchHistoryService;

    public void setSearchHistoryService(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    public RecyclerViewCommonInterface getCommonInterface() {
        return commonInterface;
    }

    public void setCommonInterface(RecyclerViewCommonInterface commonInterface) {
        this.commonInterface = commonInterface;
    }

    public SearchHistoryAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public SearchHistoryAdapter(Context context, List<SearchHistoryBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<SearchHistoryBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public void setSelectedList(List<SearchHistoryBean> items) {
        this.selectedList.clear();
        this.selectedList.addAll(items);
        notifyDataSetChanged();
    }

    public  List<SearchHistoryBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.item_search_history, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

            ((ImageViewHolder) viewHolder).tv_title.setText(list.get(position).getName());
            ((ImageViewHolder) viewHolder).iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (searchHistoryService!=null){
                        searchHistoryService.deleteByItem(list.get(position));
                        list.remove(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tv_title;
        @BindView(R.id.iv_delete)
        ImageView iv_delete;

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
