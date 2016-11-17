package com.feicuiedu.com.easyshop.main.me.goodsload;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.feicuiedu.com.easyshop.R;
import com.feicuiedu.com.easyshop.model.ImageItem;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GoodsLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    public static final int MODE_NORMAL = 1;
    public static final int MODE_MULTI_SELECT = 2;

    public enum ITEM_TYPE {ITEM_NORMAl, ITEM_ADD}

    public int mode;
    private ArrayList<ImageItem> list = new ArrayList<>();
    private final LayoutInflater inflater;
    private OnItemClickedListener mListener;

    public GoodsLoadAdapter(Context context, ArrayList<ImageItem> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public void setListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    public void add(ImageItem photo) {
        list.add(photo);
    }

    public int getSize() {
        return list.size();
    }

    public ArrayList<ImageItem> getList() {
        return list;
    }

    public void notifyData() {
        notifyDataSetChanged();
    }

    public void notifyDataSet() {
        notifyDataSetChanged();
    }

    @SuppressWarnings("SameParameterValue")
    public void changeMode(int mode) {
        this.mode = mode;
        notifyDataSetChanged();
    }

    public int getMode() {
        return mode;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_NORMAl.ordinal()) {
            return new ItemSelectViewHolder(inflater.inflate(R.layout.layout_item_recyclerview, parent, false));
        } else {
            return new ItemAddViewHolder(inflater.inflate(R.layout.layout_item_recyclerviewlast, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) return ITEM_TYPE.ITEM_ADD.ordinal();
        return ITEM_TYPE.ITEM_NORMAl.ordinal();
    }

    @SuppressLint("RecyclerView")
    @SuppressWarnings("deprecation")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemSelectViewHolder) {
            final ImageItem photo = list.get(position);
            final ItemSelectViewHolder item_select = (ItemSelectViewHolder) holder;
            item_select.photo = photo;
            if (mode == MODE_MULTI_SELECT) {
                item_select.checkBox.setVisibility(View.VISIBLE);
                item_select.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        list.get(position).setIsCheck(isChecked);
                    }
                });
                item_select.checkBox.setChecked(photo.isCheck());
            } else if (mode == MODE_NORMAL) {
                item_select.checkBox.setVisibility(View.GONE);
            }
            item_select.ivPhoto.setImageBitmap(photo.getBitmap());
            /*图片长按事件的监听*/
            item_select.ivPhoto.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mode = MODE_MULTI_SELECT;
                    notifyDataSetChanged();
                    if (mListener != null) {
                        mListener.onLongClicked();
                    }
                    return false;
                }
            });
            /*图片单击事件的监听*/
            item_select.ivPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onPhotoClicked(item_select.photo, item_select.ivPhoto);
                    }
                }
            });
        } else if (holder instanceof ItemAddViewHolder) {
            final ItemAddViewHolder item_add = (ItemAddViewHolder) holder;
            if (position == 8) {
                item_add.ib_add.setVisibility(View.GONE);
            } else {
                item_add.ib_add.setVisibility(View.VISIBLE);
            }
            item_add.ib_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onAddClicked();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return Math.min(list.size() + 1, 8);
    }

    /**
     * RecycleView中item点击事件
     */
    public interface OnItemClickedListener {

        /**
         * 单击图片的监听事件
         *
         * @param photo     {@link ImageItem}
         * @param imageView 点击图片对应的ImageView
         */
        void onPhotoClicked(ImageItem photo, ImageView imageView);

        /**
         * 添加按钮的监听事件
         */
        void onAddClicked();

        /**
         * 长按照片的监听事件
         */
        void onLongClicked();
    }

    /**
     * 图片布局
     */
    public static class ItemSelectViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.cb_check_photo)
        CheckBox checkBox;
        ImageItem photo;

        public ItemSelectViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**
     * 商品图片添加按钮布局
     */
    public static class ItemAddViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ib_recycle_add)
        ImageButton ib_add;

        public ItemAddViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
