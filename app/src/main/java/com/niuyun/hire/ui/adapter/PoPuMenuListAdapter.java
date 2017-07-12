package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.bean.GoodsFilterBean;

import java.util.List;

/**
 * Created by liu.zhenrong on 2017/2/14.
 */

public class PoPuMenuListAdapter extends BaseAdapter{
    private Context context;

    private ViewHolder viewHolder;

    private List<GoodsFilterBean> list;

    public PoPuMenuListAdapter(Context context, List<GoodsFilterBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_popu_list, null);
        }

        viewHolder.text1 = (TextView) convertView.findViewById(R.id.textname);
        viewHolder.text1.setText(list.get(position).getTypename());

        return convertView;
    }

    private class ViewHolder {
        private TextView text1;
    }
}
