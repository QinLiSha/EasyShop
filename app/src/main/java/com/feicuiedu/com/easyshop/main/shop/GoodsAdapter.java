package com.feicuiedu.com.easyshop.main.shop;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.components.AvatarLoadOptions;
import com.feicuiedu.com.easyshop.model.GoodsInfo;
import com.feicuiedu.com.easyshop.network.EasyShopApi;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品展示的适配器
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.GoodsView> {

    private final List<GoodsInfo> list = new ArrayList<>();
    private OnItemClickedListener listener;
    private Context context;

    public void setListener(OnItemClickedListener listener) {
        this.listener = listener;
    }

    public void addData(List<GoodsInfo> data) {
        list.addAll(data);
        notifyDataSetChanged();
    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    @Override
    public GoodsView onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler, parent, false);
        return new GoodsView(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(GoodsView holder, final int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_price.setText(context.getString(R.string.goods_money, list.get(position).getPrice()));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onPhotoClicked(list.get(position));
            }
        });
        ImageLoader.getInstance()
                .displayImage(EasyShopApi.IMAGE_URL + list.get(position).getPage(),
                        holder.imageView, AvatarLoadOptions.build_item());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @SuppressWarnings("unused")
    public static class GoodsView extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_item_recycler)
        ImageView imageView;
        @Bind(R.id.tv_item_name)
        TextView tv_name;
        @Bind(R.id.tv_item_price)
        TextView tv_price;

        public GoodsView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 单击照片的监听事件
     */
    public interface OnItemClickedListener {
        void onPhotoClicked(GoodsInfo goodsInfo);
    }
}
