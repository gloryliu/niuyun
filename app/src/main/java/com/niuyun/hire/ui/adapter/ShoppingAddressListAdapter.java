package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.activity.ShoppingAddressEditActivity;
import com.niuyun.hire.ui.bean.ShoppingAddressListItemBean;
import com.niuyun.hire.ui.listerner.ShoppingAddressItemOnClickListerner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class ShoppingAddressListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<ShoppingAddressListItemBean> list;
    private static Context context;
    private String type;
    private ShoppingAddressItemOnClickListerner onClickListerner;

//    public ShoppingAddressItemDeleteListerner getOnDeleteListerner() {
//        return onDeleteListerner;
//    }
//
//    public void setOnDeleteListerner(ShoppingAddressItemDeleteListerner onDeleteListerner) {
//        this.onDeleteListerner = onDeleteListerner;
//    }
//
//    private ShoppingAddressItemDeleteListerner onDeleteListerner;

    public ShoppingAddressItemOnClickListerner getOnClickListerner() {
        return onClickListerner;
    }

    public void setOnClickListerner(ShoppingAddressItemOnClickListerner onClickListerner) {
        this.onClickListerner = onClickListerner;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private final LayoutInflater mLayoutInflater;


    public ShoppingAddressListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public ShoppingAddressListAdapter(Context context, List<ShoppingAddressListItemBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<ShoppingAddressListItemBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<ShoppingAddressListItemBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.activity_shopping_address, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {

            ((ImageViewHolder) viewHolder).tv_address_name.setText(list.get(position).getName());
            ((ImageViewHolder) viewHolder).tv_address_phone.setText(list.get(position).getPhone());
            ((ImageViewHolder) viewHolder).tv_address_detail.setText(list.get(position).getDetail());
            ((ImageViewHolder) viewHolder).tv_address_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //编辑
                    Intent intent = new Intent(context, ShoppingAddressEditActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("flag", "1");
                    bundle.putString("phone", list.get(position).getPhone());
                    bundle.putString("name", list.get(position).getName());
                    bundle.putString("detail", list.get(position).getDetail());
                    bundle.putString("id", list.get(position).getId() + "");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
            ((ImageViewHolder) viewHolder).tv_address_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //删除
//                    if (onDeleteListerner != null) {
//                        onDeleteListerner.onDeleteListerner();
//                    }
                    if (onClickListerner != null)
                        onClickListerner.onDeleteListerner(list.get(position));
                }
            });
            if (!TextUtils.isEmpty(type) && type.equals("getAddress")) {
                ((ImageViewHolder) viewHolder).ll_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onClickListerner != null)
                            onClickListerner.onClick(list.get(position));
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_address_name)
        TextView tv_address_name;
        @BindView(R.id.tv_address_phone)
        TextView tv_address_phone;
        @BindView(R.id.tv_address_detail)
        TextView tv_address_detail;
        @BindView(R.id.ll_address)
        LinearLayout ll_address;
        @BindView(R.id.tv_address_edit)
        TextView tv_address_edit;
        @BindView(R.id.tv_address_delete)
        TextView tv_address_delete;

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
