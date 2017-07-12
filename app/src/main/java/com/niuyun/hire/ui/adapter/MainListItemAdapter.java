package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.activity.GoodsDetailsActivity;
import com.niuyun.hire.ui.bean.GoodsListBean;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class MainListItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<GoodsListBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public MainListItemAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public MainListItemAdapter(Context context, List<GoodsListBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<GoodsListBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<GoodsListBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_item_home_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            try {
                ImageLoadedrManager.getInstance().display(context, list.get(position).getGoodsImg(), ((ImageViewHolder) viewHolder).iv_image);
            } catch (Exception e) {

            }
            if (!TextUtils.isEmpty(list.get(position).getName())) {

                ((ImageViewHolder) viewHolder).tv_title.setText(list.get(position).getName());
                ((ImageViewHolder) viewHolder).tv_title.setVisibility(View.VISIBLE);
            } else {
                ((ImageViewHolder) viewHolder).tv_title.setVisibility(View.GONE);
            }
            ((ImageViewHolder) viewHolder).iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, GoodsDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detail", list.get(position));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });
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
//                    int[] startingLocation = new int[2];
//                    v.getLocationOnScreen(startingLocation);
//                    startingLocation[0] += v.getWidth() / 2;
//                    StoriesBean storiesEntity = new StoriesBean();
//                    storiesEntity.setId(MainNewsItemAdapter.getEntities().get(getPosition()).getId());
//                    storiesEntity.setTitle(MainNewsItemAdapter.getEntities().get(getPosition()).getTitle());
//                    Bundle bundle = new Bundle();
//                    bundle.putIntArray(Constant.START_LOCATION, startingLocation);
//                    bundle.putSerializable("entity",storiesEntity);
//                    bundle.putBoolean("isLight", ((MainActivity) context).isLight());
//                    LogUtils.e("isLight:"+((MainActivity) context).isLight());
//                    AppContext.getInstance().intentJump((Activity) MainNewsItemAdapter.context,LatestContentActivity.class,bundle);
                }
            });
        }
    }

}
