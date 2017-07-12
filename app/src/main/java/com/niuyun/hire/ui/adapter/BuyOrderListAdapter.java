package com.niuyun.hire.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.niuyun.hire.R;
import com.niuyun.hire.ui.activity.OrderDetailsActivity;
import com.niuyun.hire.ui.bean.BuyOrderListItemBean;
import com.niuyun.hire.utils.ImageLoadedrManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by chenzhiwei 2016/6/14.
 */
public class BuyOrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<BuyOrderListItemBean.DataBean> list;
    private static Context context;
    private boolean isLight;
    private final LayoutInflater mLayoutInflater;

    public BuyOrderListAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(context);
    }

    public BuyOrderListAdapter(Context context, List<BuyOrderListItemBean.DataBean> items) {
        this.context = context;
        this.list = new ArrayList<>();
        this.list.addAll(items);
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void addList(List<BuyOrderListItemBean.DataBean> items) {
        this.list.addAll(items);
        notifyDataSetChanged();
    }

    public void ClearData() {
        list.clear();
        notifyDataSetChanged();
    }

    public static List<BuyOrderListItemBean.DataBean> getEntities() {
        return list;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(mLayoutInflater.inflate(R.layout.fragment_item_buy_order_list, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        if (list != null) {
            ImageLoadedrManager.getInstance().display(context, list.get(position).getGoodsimg(), ((ImageViewHolder) viewHolder).iv_goods);
            ((ImageViewHolder) viewHolder).tv_store_name.setText(list.get(position).getShopName());
            ((ImageViewHolder) viewHolder).tv_goods_name.setText(list.get(position).getGoodsName());
            ((ImageViewHolder) viewHolder).tv_goods_price.setText(list.get(position).getPurchase()/100.00 + "");
            ((ImageViewHolder) viewHolder).tv_goods_number.setText("x" + list.get(position).getCount());
            ((ImageViewHolder) viewHolder).tv_translate_state.setText(list.get(position).getOrderStatus());
            ((ImageViewHolder) viewHolder).tv_goods_cost.setText("共计" + list.get(position).getCount() + "件商品，合计" + list.get(position).getTotalmoney()/100.00 + "元");
            ((ImageViewHolder) viewHolder).ll_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, OrderDetailsActivity.class);
                    intent.putExtra("orderId", list.get(position).getId() + "");
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
        @BindView(R.id.iv_goods)
        ImageView iv_goods;//商品图片
        @BindView(R.id.tv_store_name)
        TextView tv_store_name;//店铺名称
        @BindView(R.id.tv_goods_name)
        TextView tv_goods_name;//商品名称
        @BindView(R.id.tv_goods_price)
        TextView tv_goods_price;//商品价格
        @BindView(R.id.tv_goods_number)
        TextView tv_goods_number;//商品数量
        @BindView(R.id.tv_goods_cost)
        TextView tv_goods_cost;//商品数量，总价
        @BindView(R.id.ll_content)//全局
                LinearLayout ll_content;
        @BindView(R.id.tv_translate_state)//交易状态
                TextView tv_translate_state;

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
