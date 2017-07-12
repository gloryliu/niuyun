package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.activity.GoodsDetailsActivity;
import com.niuyun.hire.ui.bean.HomeListBean;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class MainListItemAdapters extends BaseAdapter {
    private static List<HomeListBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public MainListItemAdapters(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public MainListItemAdapters(Context context, List<HomeListBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<HomeListBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<HomeListBean> getEntities() {
        return list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.fragment_item_home_list, null);
            viewHolder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (list != null) {
            try {
                ImageLoadedrManager.getInstance().display(context, list.get(position).getImageId(), (viewHolder).iv_image);
            } catch (Exception e) {

            }
            if (!TextUtils.isEmpty(list.get(position).getTitle())) {

                (viewHolder).tv_title.setText(list.get(position).getTitle());
                (viewHolder).tv_title.setVisibility(View.VISIBLE);
            } else {
                (viewHolder).tv_title.setVisibility(View.GONE);
            }
            (viewHolder).iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, GoodsDetailsActivity.class));
                }
            });
        }
        return convertView;
    }


    private class ViewHolder {
        //        @BindView(R.id.iv_image)
        ImageView iv_image;
        //        @BindView(R.id.tv_title)
        TextView tv_title;

    }

}
