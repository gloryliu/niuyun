package com.niuyun.hire.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class MyBaseAdapter<T> extends BaseAdapter {
    protected final int mItemLayoutId;
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mData;

    public MyBaseAdapter(Context context, int itemLayoutId) {
        this(context, null, itemLayoutId);
    }


    public MyBaseAdapter(Context context, List<T> mData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mItemLayoutId = itemLayoutId;
        this.mData = mData;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData == null || mData.size() == 0 ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();

    }


    public abstract void convert(MyViewHolder helper, T item, int position);

    private MyViewHolder getViewHolder(int position, View convertView,
                                       ViewGroup parent) {
        return MyViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}
